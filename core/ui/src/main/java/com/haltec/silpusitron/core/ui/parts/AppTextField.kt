package com.haltec.silpusitron.core.ui.parts

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getAppTextFieldColors(
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors()
) :  TextFieldColors{
    return colors.copy(
        disabledContainerColor = DisabledInputContainer,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    )
}