package com.haltec.silpusitron.core.ui.parts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.domain.model.TextValidationType
import com.haltec.silpusitron.core.ui.R


@Composable
fun <T, V> ErrorValidationText(
    data: InputTextData<T, V>,
    labelName: String,
    modifier: Modifier = Modifier
) {
    if (!data.isValid) {
        val string: String? =
            when (val validation = data.validationError) {
                is TextValidationType.Required -> {
                    stringResource(id = R.string.is_required, labelName)
                }
                is TextValidationType.MinLength -> {
                    stringResource(id = R.string.min_length_required, labelName, validation.minLength)
                }
                 is TextValidationType.ExactLength -> {
                     stringResource(id = R.string.n_length_required, labelName, validation.length)
                 }
                TextValidationType.Email -> null //TODO()
                TextValidationType.Invalid -> null //TODO()
                is TextValidationType.MaxLength -> null //TODO()
                is TextValidationType.MaxValue -> null //TODO()
                is TextValidationType.MinValue -> null //TODO()
                else -> null
            }
        Text(
            text = string ?: data.message ?: stringResource(R.string.unknown_error),
            modifier = modifier,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp
        )

    } else Unit
}