package com.haltec.silpusitron.feature.auth.common.data

import com.haltec.silpusitron.common.util.DispatcherProvider
import com.haltec.silpusitron.data.mechanism.NetworkBoundResource
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.auth.common.data.remote.AuthRemoteDataSource
import com.haltec.silpusitron.feature.auth.common.domain.IAuthRepository
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginRequest
import com.haltec.silpusitron.feature.auth.login.data.remote.LoginResponse
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginResult
import com.haltec.silpusitron.feature.auth.otp.data.remote.RequestOTPResponse
import com.haltec.silpusitron.feature.auth.otp.data.remote.VerifyOTPRequest
import com.haltec.silpusitron.feature.auth.otp.data.remote.VerifyOTPResponse
import com.haltec.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
import com.haltec.silpusitron.shared.auth.preference.AuthPreference
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.shared.form.domain.model.valueOrEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
                preference.storeAuth(username = responseData.data?.username ?: "", token = responseData.data?.token  ?: "")
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

    override fun verifyOTP(otp: String): Flow<Resource<Boolean>> {
        return object: NetworkBoundResource<Boolean, VerifyOTPResponse>(){
            override suspend fun requestFromRemote(): Result<VerifyOTPResponse> {
                return remoteDataSource.verifyOTP(
                    VerifyOTPRequest(otp),
                    preference.getToken().first()
                )
            }

            override fun loadResult(responseData: VerifyOTPResponse): Flow<Boolean> {
                return flowOf(true)
            }

            override suspend fun onSuccess(responseData: VerifyOTPResponse) {
                preference.storeAuth(
                    username = responseData.data?.username ?: "",
                    token = responseData.data?.token  ?: "",
                    otpValid = true
                )
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

    override fun requestOTP(): Flow<Resource<RequestOTPResult>> {
        return object: NetworkBoundResource<RequestOTPResult, RequestOTPResponse>(){
            override suspend fun requestFromRemote(): Result<RequestOTPResponse> {
                return remoteDataSource.requestOTP(preference.getToken().first())
            }

            override fun loadResult(responseData: RequestOTPResponse): Flow<RequestOTPResult> {
                return flowOf(RequestOTPResult(responseData.data?.otpTime ?: 0L))
            }

            override suspend fun onSuccess(responseData: RequestOTPResponse) {
                preference.storeAuth(username = responseData.data?.username ?: "", token = responseData.data?.token  ?: "")
            }

        }
            .asFlow()
            .flowOn(dispatcher.io)
    }

}