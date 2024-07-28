package com.haltec.silpusitron.shared.lettertype

import com.haltec.silpusitron.shared.lettertype.data.LetterTypesRemoteDataSource
import com.haltec.silpusitron.shared.lettertype.data.LetterTypesRepository
import com.haltec.silpusitron.shared.lettertype.data.LetterTypesService
import com.haltec.silpusitron.shared.lettertype.domain.GetLetterTypeOptionsUseCase
import com.haltec.silpusitron.shared.lettertype.domain.ILetterTypesRepository
import org.koin.dsl.module

val letterTypeModule = module {
    factory { LetterTypesService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { LetterTypesRemoteDataSource(get()) }
    factory<ILetterTypesRepository> { LetterTypesRepository(get(), get()) }
    factory { GetLetterTypeOptionsUseCase() }
}