package com.silpusitron.feature.auth.common.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.common.data.remote.AuthRemoteDataSource
import com.silpusitron.feature.auth.common.domain.IAuthRepository
import com.silpusitron.feature.auth.common.domain.UserType
import com.silpusitron.feature.auth.login.data.remote.LoginRequest
import com.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.silpusitron.feature.auth.login.domain.model.LoginResult
import com.silpusitron.shared.auth.preference.AuthPreference
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType
import com.silpusitron.shared.form.domain.model.valueOrEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: AuthRemoteDataSource,
    private val preference: AuthPreference
): IAuthRepository {
    override fun login(
        username: InputTextData<TextValidationType, String>,
        password: InputTextData<TextValidationType, String>,
        captcha: InputTextData<TextValidationType, String>,
        userType: UserType
    ): Flow<Resource<LoginResult>> {
        return object: NetworkBoundResource<LoginResult, LoginResponse>(){
            override suspend fun requestFromRemote(): Result<LoginResponse> {
                return remoteDataSource.login(
                    LoginRequest(
                        username = username.valueOrEmpty(),
                        password = password.valueOrEmpty(),
                        captcha = captcha.valueOrEmpty(),
                        userType = userType.value
                    )
                )
            }

            override fun loadResult(responseData: LoginResponse): Flow<LoginResult> {
                return flowOf(responseData.toModel(username, password, captcha, userType))
            }

            override suspend fun onSuccess(responseData: LoginResponse) {
                preference.storeAuthWithPhoneNumber(
                    username = responseData.data?.username ?: "",
                    token = responseData.data?.token  ?: "",
                    phoneNumber = responseData.data?.phoneNumber ?: ""
                )
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun checkSession(): Flow<Boolean> {
        return preference.isSessionValid()
    }

    override suspend fun logout() {
        withContext(dispatcher.io){
            preference.resetAuth()
        }
    }

}