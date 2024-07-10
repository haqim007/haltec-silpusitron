package com.haltec.silpusitron.shared.form.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.shared.form.domain.model.InputTextData
import com.haltec.silpusitron.shared.form.domain.model.TextValidationType


@Composable
fun <T, V> ErrorValidationText(
    data: InputTextData<T, V>,
    labelName: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp
) {
    if (!data.isValid) {
        val string: String? =
            when (val validation = data.validationError) {
                is TextValidationType.Required -> {
                    stringResource(id = R.string.is_required, labelName)
                }
                is TextValidationType.MinLength -> {
                    stringResource(id = R.string.min_length_n_chars, validation.minLength)
                }
                 is TextValidationType.ExactLength -> {
                     stringResource(id = R.string.n_length_required, labelName, validation.length)
                 }
                TextValidationType.Email -> null //TODO()
                TextValidationType.Invalid -> null //TODO()
                is TextValidationType.MaxLength ->
                    stringResource(id = R.string.max_length_n_chars, validation.maxLength)
                is TextValidationType.MaxValue ->
                    stringResource(id = R.string.max_value_n, "${validation.maxValue}")
                is TextValidationType.MinValue ->
                    stringResource(id = R.string.min_value_n, "${validation.minValue}")
                else -> null
            }
        Text(
            text = string ?: data.message ?: stringResource(R.string.unknown_error),
            modifier = modifier,
            color = MaterialTheme.colorScheme.error,
            fontSize = fontSize
        )

    } else Unit
}