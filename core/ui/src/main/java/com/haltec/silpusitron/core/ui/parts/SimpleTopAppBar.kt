package com.haltec.silpusitron.core.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.backgroundGradient
import com.haltec.silpusitron.core.ui.theme.gradientColors

@Composable
fun SimpleTopAppBar(
    title: String,
    prependContent: (@Composable (modifier: Modifier) -> Unit)? = null,
    onNavigateBack: (() -> Unit)? = null
){
    Row(
        Modifier
            .backgroundGradient(
                shape = onNavigateBack?.let {
                    RoundedCornerShape(bottomStart = 20.dp)
                }  ?: RectangleShape
            )
    ) {
        onNavigateBack?.let {
            IconButton(
                modifier = Modifier.weight(0.75f),
                onClick = { it() },
                colors = IconButtonDefaults.iconButtonColors()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        SmallTopBar(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.height(50.dp).weight(5f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                maxLines = 1,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE
                )
            )
        }

        prependContent?.let { it(Modifier.weight(1f)) }
    }
}