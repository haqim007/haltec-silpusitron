package com.haltec.silpusitron.shared.auth.mechanism

import com.haltec.silpusitron.data.mechanism.CustomThrowable
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import io.ktor.http.HttpStatusCode

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