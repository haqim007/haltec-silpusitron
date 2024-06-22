package com.haltec.silpusitron.core.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.theme.AppTypography
import com.haltec.silpusitron.core.ui.theme.OnPrimaryLight

@Composable
fun ContainerWithBanner(
    containerModifier: Modifier = Modifier,
    bannerModifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Box(
        modifier = containerModifier
            .fillMaxSize()
    ) {
        Banner(bannerModifier)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = sharedModifier
                    .padding(top = 54.dp)
                    .height(57.dp)
            )

            Text(
                text = stringResource(id = R.string.app_title),
                color = OnPrimaryLight,
                style = AppTypography.headlineSmall.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .width(157.dp)
                    .padding(top = 50.dp),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            content()
        }
    }
}