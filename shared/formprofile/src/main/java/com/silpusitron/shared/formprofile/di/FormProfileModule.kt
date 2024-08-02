package com.silpusitron.shared.formprofile.di

import com.silpusitron.shared.auth.di.authSharedModule
import com.silpusitron.shared.district.di.districtModule
import com.silpusitron.shared.formprofile.data.FormProfileRepository
import com.silpusitron.shared.formprofile.data.remote.FormProfileRemoteDataSource
import com.silpusitron.shared.formprofile.data.remote.FormProfileService
import com.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import com.silpusitron.shared.formprofile.domain.usecase.GetBloodTypeOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetEducationOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetFamRelationStatusOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetGenderOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetMarriageStatusOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetProffesionOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetProfileUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetReligionOptionsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.GetSubDistrictsUseCase
import com.silpusitron.shared.formprofile.domain.usecase.SubmitProfileUseCase
import com.silpusitron.shared.formprofile.domain.usecase.ValidateAllInputUseCase
import com.silpusitron.shared.formprofile.ui.FormProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val formProfileModule = module {
    includes(authSharedModule)
    factory { FormProfileService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { FormProfileRemoteDataSource(get()) }
    factory<IFormProfileRepository> {
        com.silpusitron.shared.formprofile.data.FormProfileRepository(
            get(),
            get(),
            get()
        )
    }
    factory { GetProfileUseCase() }
    factory { GetGenderOptionsUseCase() }
    factory { GetProffesionOptionsUseCase() }
    factory { GetReligionOptionsUseCase() }
    factory { GetBloodTypeOptionsUseCase() }
    factory { GetFamRelationStatusOptionsUseCase() }
    factory { GetMarriageStatusOptionsUseCase() }
    factory { GetEducationOptionsUseCase() }
    includes(districtModule)
    factory { GetSubDistrictsUseCase() }
    factory { ValidateAllInputUseCase() }
    factory { SubmitProfileUseCase() }
    viewModel {
        FormProfileViewModel(
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get(),
            get(), get()
        )
    }
}