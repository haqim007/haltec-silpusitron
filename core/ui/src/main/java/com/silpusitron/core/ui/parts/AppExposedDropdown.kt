package com.silpusitron.core.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue


data class AppExposedDropdownModel(
    val label: String, val value: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppExposedDropdown(
    value: String,
    options: List<AppExposedDropdownModel>,
    onSelect: (option: AppExposedDropdownModel) -> Unit,
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable ((
            setExpand: (expand: Boolean) -> Unit,
            defaultIcon: @Composable () -> Unit
    ) -> Unit)? = null,
    readOnly: Boolean = true,
    enabled: Boolean = true
){

    var expanded by remember {
        mutableStateOf(false)
    }

    var valueText by remember {
        mutableStateOf(
            TextFieldValue(
                text = options.firstOrNull { it.value == value }?.label ?: ""
            )
        )
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = if (enabled) !expanded else expanded
        },
        modifier = Modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = valueText,
            onValueChange = {},
            readOnly = readOnly,
            singleLine = true,
            label = label,
            trailingIcon = {
                if(trailingIcon != null){
                    trailingIcon(
                        {isExpand ->
                            expanded = isExpand
                        },
                        {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            colors = getAppTextFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        onSelect(option)
                        valueText =
                            TextFieldValue(
                                text = option.label,
                                selection = TextRange(option.label.length),
                            )
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}