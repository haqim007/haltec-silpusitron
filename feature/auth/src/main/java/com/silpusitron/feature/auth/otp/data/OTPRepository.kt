package com.silpusitron.feature.auth.otp.data

import com.silpusitron.common.util.DispatcherProvider
import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.otp.data.remote.OTPRemoteDataSource
import com.silpusitron.feature.auth.otp.data.remote.RequestOTPResponse
import com.silpusitron.feature.auth.otp.data.remote.VerifyOTPRequest
import com.silpusitron.feature.auth.otp.data.remote.VerifyOTPResponse
import com.silpusitron.feature.auth.otp.domain.IOTPRepository
import com.silpusitron.feature.auth.otp.domain.model.RequestOTPResult
import com.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class OTPRepository(
    private val dispatcher: DispatcherProvider,
    private val remoteDataSource: OTPRemoteDataSource,
    private val preference: AuthPreference
): IOTPRepository {

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
                preference.storeAuthOnOTPValid(
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
                return flow {
                    val phoneNumber = preference.getPhoneNumber().first()
                    emit(RequestOTPResult(responseData.data?.otpTime ?: 0L, phoneNumber))
                }
            }

            override suspend fun onSuccess(responseData: RequestOTPResponse) {
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

}