package com.silpusitron.core.ui.parts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silpusitron.core.ui.R
import com.silpusitron.core.ui.component.Banner
import com.silpusitron.core.ui.theme.AppTypography
import com.silpusitron.core.ui.theme.OnPrimaryLight
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import kotlinx.coroutines.delay

@Composable
fun ContainerWithBanner(
    modifier: Modifier = Modifier,
    bannerModifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    withWelcome: Boolean = false,
    scrollable: Boolean = true,
    onBannerInvisible: (isVisible: Boolean) -> Unit = {},
    content: @Composable () -> Unit
){

    var withWelcomeState by rememberSaveable {
        mutableStateOf(false)
    }

    val density = LocalDensity.current

    val scale by animateFloatAsState(
        targetValue = if (withWelcomeState) 0.75f else 1f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500
        ),
        label = "floatAnimationLogo"
    )
    
    val topPaddingLogo by animateDpAsState(
        targetValue = if (withWelcomeState) 30.dp else 50.dp,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 500
        ),
        label = "topPaddingLogo"
    )

    LaunchedEffect(key1 = Unit) {
        delay(500)
        withWelcomeState = withWelcome
    }
    val scrollState = rememberScrollState()
    val hideBanner by remember {
        derivedStateOf {
            scrollState.value >= 500
        }
    }

    LaunchedEffect(key1 = hideBanner) {
        onBannerInvisible(hideBanner)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxSize()
                .then(
                    if (scrollable) Modifier.verticalScroll(scrollState) else Modifier
                )
        ) {

            Banner(bannerModifier.height(270.dp)){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = sharedModifier
                            .padding(top = topPaddingLogo)
                            .height(57.dp)
                            .scale(scale)
                    )

                    AnimatedVisibility(
                        visible = withWelcomeState,
                        enter = slideInVertically(
                            animationSpec = tween(durationMillis = 2000, delayMillis = 1000)
                        ) {
                            // Slide in from 50 dp from the top.
                            with(density) { -50.dp.roundToPx() }
                        },
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcome),
                            color = OnPrimaryLight,
                            style = AppTypography.headlineSmall.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(top = 20.dp),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }

                    AnimatedVisibility(
                        visible = if (withWelcome) withWelcomeState else true,
                        enter = slideInVertically(
                            animationSpec = tween(durationMillis = 2000, delayMillis = 2000)
                        ) {
                            // Slide in from 100 dp from the top.
                            with(density) { -100.dp.roundToPx() }
                        },
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_title),
                            color = OnPrimaryLight,
                            style = AppTypography.headlineSmall.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .width(157.dp)
                                .padding(top = if (withWelcomeState) 25.dp else 50.dp),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContainerWithBannerPreview(){
    SILPUSITRONTheme {
        ContainerWithBanner(
            modifier = Modifier.fillMaxSize(),
            bannerModifier = Modifier.height(270.dp),
            withWelcome = true
        ){
            Text(text = "Hehehe")
        }
    }
}