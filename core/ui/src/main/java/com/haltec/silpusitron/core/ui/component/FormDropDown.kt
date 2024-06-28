package com.haltec.silpusitron.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.haltec.silpusitron.core.domain.model.InputTextData
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <ValidationType> FormDropDown(
    label: String,
    value: String,
    onValueChange: (InputTextData.Option) -> Unit,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors().copy(
        disabledContainerColor = DisabledInputContainer,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    ),
    isLoading: Boolean = false,
    inputData: InputTextData<ValidationType, String>,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean? = null,
    editable: Boolean = false
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
            label = label,
            inputData = inputData,
            colors = colors,
            trailingIcon = trailingIcon ?: {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
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