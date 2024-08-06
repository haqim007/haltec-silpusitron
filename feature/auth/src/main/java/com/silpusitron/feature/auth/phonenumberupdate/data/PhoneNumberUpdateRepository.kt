package com.silpusitron.feature.auth.phonenumberupdate.data

import com.silpusitron.data.mechanism.NetworkBoundResource
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.phonenumberupdate.data.remote.PhoneNumberUpdateRemoteDataSource
import com.silpusitron.feature.auth.phonenumberupdate.data.remote.PhoneNumberUpdateRequest
import com.silpusitron.feature.auth.phonenumberupdate.data.remote.PhoneNumberUpdateResponse
import com.silpusitron.feature.auth.phonenumberupdate.domain.repository.IPhoneNumberRepository
import com.silpusitron.shared.auth.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PhoneNumberUpdateRepository(
    private val preference: AuthPreference,
    private val remoteDataSource: PhoneNumberUpdateRemoteDataSource
): IPhoneNumberRepository {
    override fun update(phoneNumber: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String, PhoneNumberUpdateResponse>() {

            private suspend fun getToken(): String{
                return preference.getToken().first()
            }

            override suspend fun requestFromRemote(): Result<PhoneNumberUpdateResponse> {
                return remoteDataSource.update(getToken(), PhoneNumberUpdateRequest(phoneNumber))
            }

            override fun loadResult(responseData: PhoneNumberUpdateResponse): Flow<String> {
                return flowOf(responseData.message ?: "")
            }

            override suspend fun onSuccess(responseData: PhoneNumberUpdateResponse) {
                responseData.data.let {
                    preference.storeAuthWithPhoneNumber(
                        it?.username ?: "",
                        it?.noHp ?: "",
                        it?.token ?: ""
                    )
                }
            }

        }.asFlow()
    }

}
