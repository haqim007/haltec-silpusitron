package com.silpusitron.shared.form.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.silpusitron.shared.form.domain.model.InputTextData

@Composable
fun <T, V> FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    inputLabel: String,
    inputData: InputTextData<T, V>,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.colors().copy(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = DisabledInputContainer
    ),
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    enabled: Boolean? = null,
    prefix: @Composable (() -> Unit)? = null,
    placeholder: @Composable() (() -> Unit)? = null,
){
    TextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        modifier = modifier,
        colors = colors,
        enabled = enabled ?: !isLoading,
        isError = !inputData.isValid,
        supportingText = {
            ErrorValidationText(
                data = inputData,
                inputLabel,
            )
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
        prefix = prefix,
        placeholder = placeholder
    )
}