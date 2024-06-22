package com.haltec.silpusitron.data.mechanism

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

/*
*
* RequestType: Data type that used to catch network response a.k.a inserted data type
* ResultType: Data type that expected as return data a.k.a output data type
* */
abstract class NetworkBoundResource<ResultType, ResponseType> {

    private val result: Flow<Resource<ResultType>> = flow{
        emit(Resource.Loading())
        try {
            val currentLocalData = loadCurrentLocalData()
            if(onBeforeRequest()){
                val apiResponse = requestFromRemoteRunner()
                if(apiResponse.isSuccess){
                    apiResponse.getOrNull()?.let { res ->
                        onSuccess(res)
                        emitAll(
                            loadResult(res).map {
                                Resource.Success(it)
                            }
                        )
                    }

                }else{
                    val mException = apiResponse.exceptionOrNull() as? CustomThrowable

                    if (mException != null){
                        val res = apiResponse.getOrNull()
                        onFailed(mException)
                        emit(
                            Resource.Error(
                                message = mException.message ?: "Failed to fetch data",
                                data = currentLocalData ?: res?.let { loadResult(res).first() },
                                httpCode = mException.code.value,
                            )
                        )
                    }else{
                        val exceptionFallback = apiResponse.exceptionOrNull()
                        emit(
                            Resource.Error(
                                message = exceptionFallback?.message ?: "Failed to fetch data",
                                data = currentLocalData
                            )
                        )
                    }
                    
                }
            }else{
                emit(Resource.Error(message = "Request is not allowed"))
            }
            
        }catch (e: Exception){
            onFailed()
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "Failed to request"
                )
            )
        }
    }

    /**
     * To handle how requestFromRemote will be executed
     *
     * @return
     */
    protected open suspend fun requestFromRemoteRunner(): Result<ResponseType>{
        return requestFromRemote()
    }

    protected abstract suspend fun requestFromRemote(): Result<ResponseType>

    /**
     * Load from network to be returned and consumed. Convert data from network to model here
     *
     * @param data
     * @return
     */
    protected abstract fun loadResult(responseData: ResponseType): Flow<ResultType>

    /**
     * Load current data from local storage
     *
     */
    protected open suspend fun loadCurrentLocalData(): ResultType? = null

    protected open suspend fun onSuccess(responseData: ResponseType) {}

    protected open suspend fun onFailed(exceptionOrNull: CustomThrowable? = null) {}
    
    /*
    * 
    * Will be called just before [requestFromRemote] triggered in case to perform task 
    * to determine whether to perform [requestFromRemote] or not
    * 
    * */
    protected open suspend fun onBeforeRequest(): Boolean = true

    fun asFlow(): Flow<Resource<ResultType>> = result

}
