package com.silpusitron.shared.lettertype

import com.silpusitron.shared.lettertype.data.LetterTypesRemoteDataSource
import com.silpusitron.shared.lettertype.data.LetterTypesRepository
import com.silpusitron.shared.lettertype.data.LetterTypesService
import com.silpusitron.shared.lettertype.domain.GetLetterTypeOptionsUseCase
import com.silpusitron.shared.lettertype.domain.ILetterTypesRepository
import org.koin.dsl.module

val letterTypeModule = module {
    factory { LetterTypesService(getProperty("BASE_URL"), getProperty("API_VERSION")) }
    factory { LetterTypesRemoteDataSource(get()) }
    factory<ILetterTypesRepository> { LetterTypesRepository(get(), get()) }
    factory { GetLetterTypeOptionsUseCase() }
}