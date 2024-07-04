package com.haltec.silpusitron.core.ui.parts

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.LottieLoader


@Preview
@Composable
fun LoadingView(
    loader: @Composable () -> Unit = {
        LottieLoader(
            jsonRaw = R.raw.lottie_loader,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(100.dp)
        )
    },
    message: String = stringResource(R.string.please_wait),
    messageStyle: TextStyle = TextStyle.Default
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var dotsCounter by remember {
            mutableIntStateOf(0)
        }
        val infiniteTransition = rememberInfiniteTransition(label = "dots")
        val animateDots = infiniteTransition
            .animateValue(
                initialValue = 0,
                targetValue = 4,
                typeConverter = Int.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "dots"
            )

        LaunchedEffect(key1 = animateDots.value) {
            dotsCounter = animateDots.value
        }

        //CircularProgressIndicator()
        loader()
        Text(
            message + ".".repeat(dotsCounter),
            style = messageStyle
        )
    }
}