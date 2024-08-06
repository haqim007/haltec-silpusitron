package com.silpusitron.core.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

typealias InputLabelComposable = @Composable (
    label: String,
    modifier: Modifier,
    isRequired: Boolean,
    color: Color
) -> Unit

@Composable
fun InputLabel(
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onBackground
){
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = color
                )
            ){
                append(label)
            }
            if(isRequired){
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                    append("*") // Star character
                }
            }
        },
        modifier = modifier
    )
}