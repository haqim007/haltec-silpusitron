package com.haltec.silpusitron.core.ui.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.theme.SuccessColor
import kotlinx.coroutines.delay

@Composable
fun SubmitSuccessView(
    text: @Composable (() -> Unit),
    onComplete: () -> Unit = {}
) {
    LaunchedEffect(key1 = Unit) {
        delay(1000)
        onComplete()
    }

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .width(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                MaterialTheme.colorScheme.background
            )
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = SuccessColor.copy(
                        alpha = 0.5f
                    )
                ),
                RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieLoader(
                jsonRaw = R.raw.lottie_success_sparkle,
                modifier = Modifier
                    .size(200.dp),
                iterations = 10,
                speed = 0.5f
            )

            text()
        }
    }
}

@Composable
fun SubmitSuccessView(
    message: String = stringResource(R.string.successfully_saved),
    onComplete: () -> Unit = {}
) {

    SubmitSuccessView(
        onComplete = onComplete,
        text = {
            Text(
                text = message,
                style = TextStyle.Default.copy(
                    color = SuccessColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    )
}