package com.silpusitron.feature.landingpage.ui.splash

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.silpusitron.core.ui.theme.PrimaryLight
import com.silpusitron.core.ui.theme.PrimaryVariantLight
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import kotlinx.coroutines.delay
import com.silpusitron.core.ui.R as CoreR


@Composable
fun MySplashScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit,
    sharedModifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = Unit) {
        delay(3000)
        navigate()
    }

    val listColors = listOf(
        PrimaryLight.copy(alpha = 0.5f),
        PrimaryVariantLight
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listColors
                )
            ),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = CoreR.drawable.logo),
            contentDescription = "logo",
            modifier = sharedModifier
                .width(260.dp)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SILPUSITRONTheme {
        MySplashScreen(
            navigate = {}
        )
    }
}