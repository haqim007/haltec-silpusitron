package com.haltec.silpusitron.core.ui.component

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.haltec.silpusitron.core.ui.R

@Composable
fun LottieLoader(
    modifier: Modifier = Modifier,
    @RawRes jsonRaw: Int,
    iterations: Int = LottieConstants.IterateForever
){
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            jsonRaw
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = iterations,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}