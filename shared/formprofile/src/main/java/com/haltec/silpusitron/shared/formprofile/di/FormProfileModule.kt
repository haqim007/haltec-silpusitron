package com.haltec.silpusitron.shared.formprofile.di

import com.haltec.silpusitron.shared.auth.di.authSharedModule
import com.haltec.silpusitron.shared.district.di.districtModule
import com.haltec.silpusitron.shared.formprofile.data.FormProfileRepository
import com.haltec.silpusitron.shared.formprofile.data.remote.FormProfileRemoteDataSource
import com.haltec.silpusitron.shared.formprofile.data.remote.FormProfileService
import com.haltec.silpusitron.shared.formprofile.domain.repository.IFormProfileRepository
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetBloodTypeOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetEducationOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetFamRelationStatusOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetGenderOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetMarriageStatusOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetProffesionOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetProfileUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetReligionOptionsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.GetSubDistrictsUseCase
import com.haltec.silpusitron.shared.formprofile.domain.usecase.ValidateAllInputUseCase
import com.haltec.silpusitron.shared.formprofile.ui.FormProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val formProfileModule = module {
    includes(authSharedModule)
    factory { FormProfileService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { FormProfileRemoteDataSource(get()) }
    factory<IFormProfileRepository> { FormProfileRepository(get(), get(), get()) }
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
    viewModel {
        FormProfileViewModel(
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get(),
            get(), get()
        )
    }
}