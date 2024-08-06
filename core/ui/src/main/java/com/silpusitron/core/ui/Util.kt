package com.silpusitron.core.ui

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.silpusitron.core.ui.theme.gradientColors

fun generateColorShades(startColor: Int, count: Int): List<Int> {
    val alpha = startColor and -0x1000000 // Extract alpha channel (FF)
    val baseColor = startColor and 0x00FFFFFF // Extract RGB channels

    val colors = mutableListOf<Int>()
    for (i in 0 until count) {
        // Calculate new color by interpolating RGB channels
        val r = ((baseColor shr 16) and 0xFF) * i / count
        val g = ((baseColor shr 8) and 0xFF) * i / count
        val b = (baseColor and 0xFF) * i / count

        // Combine alpha and interpolated RGB values to form new color
        val interpolatedColor = alpha or (r shl 16) or (g shl 8) or b
        colors.add(interpolatedColor)
    }
    return colors
}

@Composable
fun Modifier.backgroundGradient(shape: Shape = RectangleShape) = this.background(
    brush = Brush.linearGradient(
        gradientColors,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 1000f),
    ),
    shape = shape
)