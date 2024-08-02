package com.silpusitron.shared.form.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.InputLabel
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.shared.form.domain.model.InputTextData
import com.silpusitron.shared.form.domain.model.TextValidationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <ValidationType> FormDropDown(
    inputLabel: String,
    label: @Composable () -> Unit,
    value: String,
    onValueChange: (InputTextData.Option) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors().copy(
        disabledContainerColor = DisabledInputContainer,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    ),
    isLoading: Boolean = false,
    inputData: InputTextData<ValidationType, String>,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean? = null,
    editable: Boolean = false,
    loadNetworkError: Boolean = false,
    reloadNetwork: () -> Unit = {}
){
    var expanded by remember {
        mutableStateOf(false)
    }

    var text by remember {
        mutableStateOf(
            TextFieldValue(
                text = inputData.options?.firstOrNull { it.key == value }?.label ?: ""
            )
        )
    }
    val optionsState = if (editable) inputData.options?.filter { it.value == text.text } else inputData.options

    LaunchedEffect(key1 = inputData.options) {
        text = TextFieldValue(
            text = inputData.options?.firstOrNull { it.value == value }?.label ?: ""
        )
    }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        FormTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = text.text,
            onValueChange = {
                if (editable) {
                    text = TextFieldValue(it)
                }
            },
            singleLine = true,
            readOnly = !editable || isLoading,
            label = {
                label()
            },
            inputLabel = inputLabel,
            inputData = inputData,
            colors = colors,
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = DisabledInputContainer
                    )
                } else if(loadNetworkError){
                    IconButton(onClick = {
                        reloadNetwork()
                        expanded = false // prevent dropdown from opening
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(R.string.click_to_reload_this_options)
                        )
                    }
                }
                else if(trailingIcon != null){
                    trailingIcon()
                }else{
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                    )
                }
            },
            isLoading = isLoading,
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            optionsState?.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        text =
                            TextFieldValue(
                                text = option.label,
                                selection = TextRange(option.label.length),
                            )
                        onValueChange(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}

@Preview
@Composable
fun FormDropDownPreview(){
    SILPUSITRONTheme {
        FormDropDown(
            inputLabel = "This is label",
            label = {
                InputLabel(
                    label = "This is label",
                    isRequired = true
                )
            },
            value = "",
            onValueChange = { data -> },
            inputData = InputTextData<TextValidationType, String>(
                inputName = "",
                validations = listOf(
                    TextValidationType.Required
                ),
                value = ""
            )
        )
    }
}

@Preview
@Composable
fun FormDropDownPreviewLoading(){
    SILPUSITRONTheme {
        FormDropDown(
            inputLabel = "This is label",
            label = {
                InputLabel(
                    label = "This is label",
                    isRequired = true
                )
            },
            value = "",
            onValueChange = { data -> },
            inputData = InputTextData<TextValidationType, String>(
                inputName = "",
                validations = listOf(
                    TextValidationType.Required
                ),
                value = ""
            ),
            isLoading = true
        )
    }
}

@Preview
@Composable
fun FormDropDownPreviewLoadNetworkError(){
    SILPUSITRONTheme {
        FormDropDown(
            inputLabel = "This is label",
            label = {
                InputLabel(
                    label = "This is label",
                    isRequired = true
                )
            },
            value = "",
            onValueChange = { data -> },
            inputData = InputTextData<TextValidationType, String>(
                inputName = "",
                validations = listOf(
                    TextValidationType.Required
                ),
                value = ""
            ),
            isLoading = false,
            loadNetworkError = true
        )
    }
}