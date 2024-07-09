package com.haltec.silpusitron.data.mechanism

import com.haltec.silpusitron.data.preference.AuthPreference
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement

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
                    val excCustomThrowable = apiResponse.exceptionOrNull() as? CustomThrowable

                    if (excCustomThrowable != null){
                        onFailed(excCustomThrowable)
                        emit(
                            Resource.Error(
                                message = excCustomThrowable.message ?: "Failed to fetch data",
                                data = loadResultOnError(excCustomThrowable.dataJson),
                                httpCode = excCustomThrowable.code.value,
                            )
                        )
                    }
                    else{
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

    /**
     * Load from network when the response with errors. Convert data from network to model here
     *
     * @param data
     * @return
     */
    protected open fun loadResultOnError(responseData: JsonElement?): ResultType? = null

    fun asFlow(): Flow<Resource<ResultType>> = result

}

abstract class AuthorizedNetworkBoundResource<ResultType, ResponseType>(
    private val authPreference: AuthPreference
): NetworkBoundResource<ResultType, ResponseType>(){

    final override suspend fun requestFromRemoteRunner(): Result<ResponseType> {
//        return checkToken(authPreference, requestFromRemote = { requestFromRemote() })
        return requestFromRemote()
    }

    protected abstract suspend fun getToken(): String?

    override suspend fun onFailed(exceptionOrNull: CustomThrowable?) {
        if(exceptionOrNull?.code == HttpStatusCode.Unauthorized){
            authPreference.resetAuth()
        }
    }

}

//suspend fun <RequestType> checkToken(
//    authPreference: AuthPreference,
//    requestFromRemote: suspend () -> Result<RequestType>
//): Result<RequestType>{
//    var apiResponse: Result<RequestType>? = null
//
//    // Repeat three times in case request failed because of http code 401.
//    // Because from the network API when the token expired,
//    // it will response refreshed token instead of just (error) message
//    run repeatBlock@{
//        repeat(3){
//            apiResponse = requestFromRemote()
//            if (apiResponse?.isSuccess == false){
//                val exception = apiResponse?.exceptionOrNull() as? CustomThrowable
//                if (exception?.code == 401 && exception.data?.data != null){
//                    authPreference.updateToken(
//                        exception.token,
//                        stringToTimestamp(exception.data.data.exp, DATE_TIME_FORMAT) ?: 0
//                    )
//                    delay(3000)
//                }else{
//                    return@repeatBlock
//                }
//            }else{
//                return@repeatBlock
//            }
//        }
//    }
//
//    return apiResponse!!
//}
