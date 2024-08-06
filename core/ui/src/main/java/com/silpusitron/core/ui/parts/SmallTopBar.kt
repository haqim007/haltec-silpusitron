package com.silpusitron.core.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.silpusitron.core.ui.backgroundGradient
import com.silpusitron.core.ui.theme.gradientColors

@Composable
fun SmallTopBar(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment. Vertical = Alignment.CenterVertically,
    horizontalPadding: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(
        bottomEnd = 20.dp, bottomStart = 20.dp
    ),
    content: @Composable () -> Unit
){
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
            .backgroundGradient(
                shape = shape
            )
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = horizontalPadding)

    ) {
        content()
    }
}