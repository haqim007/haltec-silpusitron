package com.haltec.silpusitron.core.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.ui.parts.ErrorValidationText
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer

@Composable
fun <T, V> FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.colors().copy(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = DisabledInputContainer
    ),
    isLoading: Boolean = false,
    inputData: InputTextData<T, V>,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    enabled: Boolean? = null,
    prefix: @Composable (() -> Unit)? = null,
){
    TextField(
        value = value,
        label = {
            Text(
                text = label
            )
        },
        onValueChange = onValueChange,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        modifier = modifier,
        colors = colors,
        enabled = enabled ?: !isLoading,
        isError = !inputData.isValid,
        supportingText = {
            ErrorValidationText(
                data = inputData,
                label,
            )
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
        prefix = prefix
    )
}