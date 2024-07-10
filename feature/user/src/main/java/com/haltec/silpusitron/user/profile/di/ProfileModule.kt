package com.haltec.silpusitron.user.profile.di

import com.haltec.silpusitron.shared.district.di.districtModule
import com.haltec.silpusitron.user.profile.data.remote.ProfileRemoteDataSource
import com.haltec.silpusitron.user.profile.data.remote.ProfileService
import com.haltec.silpusitron.user.profile.data.repository.ProfileRepository
import com.haltec.silpusitron.user.profile.domain.IProfileRepository
import com.haltec.silpusitron.user.profile.domain.usecase.GetBloodTypeOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetEducationOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetFamRelationStatusOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetGenderOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetMarriageStatusOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetProffesionOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetProfileUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetReligionOptionsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.GetSubDistrictsUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.SubmitProfileUseCase
import com.haltec.silpusitron.user.profile.domain.usecase.ValidateAllInputUseCase
import com.haltec.silpusitron.user.profile.ui.ProfileDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val profileModule = module {
    factory { ProfileService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { ProfileRemoteDataSource(get()) }
    factory<IProfileRepository> { ProfileRepository(get(), get(), get()) }
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
    factory { SubmitProfileUseCase() }
    factory { ValidateAllInputUseCase() }
    viewModel {
        ProfileDataViewModel(
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get(),
            get(), get(), get()
        )
    }
}