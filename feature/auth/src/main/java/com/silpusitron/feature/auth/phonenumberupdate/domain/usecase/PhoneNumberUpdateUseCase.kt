package com.silpusitron.feature.auth.phonenumberupdate.domain.usecase

import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.auth.phonenumberupdate.domain.repository.IPhoneNumberRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhoneNumberUpdateUseCase: KoinComponent {
    private val repository: IPhoneNumberRepository by inject()

    operator fun invoke(phoneNumber: String): Flow<Resource<String>>{
        return repository.update(phoneNumber)
    }
}
