package com.silpusitron.shared.auth.mechanism

import com.silpusitron.data.mechanism.CustomThrowable
import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.shared.auth.preference.AuthPreference
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first

abstract class AuthorizedNetworkBoundResource<ResultType, ResponseType>(
    private val authPreference: AuthPreference
): NetworkBoundResource<ResultType, ResponseType>(){

    final override suspend fun requestFromRemoteRunner(): Result<ResponseType> {
//        return checkToken(authPreference, requestFromRemote = { requestFromRemote() })
        return requestFromRemote()
    }

    open suspend fun getToken(): String{
        return authPreference.getToken().first() ?: ""
    }

    override suspend fun onFailed(exceptionOrNull: CustomThrowable?) {
        if(exceptionOrNull?.code == HttpStatusCode.Unauthorized){
            authPreference.resetAuth()
        }
    }

}