package com.haltec.silpusitron.feature.auth.login.domain.usecase

import com.haltec.silpusitron.shared.form.domain.model.TextValidationType
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.auth.common.domain.IAuthRepository
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginInputData
import com.haltec.silpusitron.shared.form.domain.model.validate
import com.haltec.silpusitron.feature.auth.login.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LoginUseCase : KoinComponent{
    private val repository: IAuthRepository by inject<IAuthRepository>()

    operator fun invoke(
        username: InputTextData<TextValidationType, String>,
        password: InputTextData<TextValidationType, String>,
        captcha: InputTextData<TextValidationType, String>,
        userType: UserType
    ): Flow<Resource<LoginResult>>{
        val validatedUsername = username.validate()
        val validatedPassword = password.validate()
        val validatedCaptcha = captcha.validate()
        if (
                !validatedUsername.isValid ||
                !validatedPassword.isValid ||
                !validatedCaptcha.isValid
            ){
            return flowOf(
                Resource.Error(
                    message = "Validasi gagal. Silahkan coba kembali",
                    data = LoginResult(
                        inputData = LoginInputData(
                            username, password, captcha, userType
                        )
                    )
                )
            )
        }

        return repository.login(username, password, captcha, userType)
    }
}