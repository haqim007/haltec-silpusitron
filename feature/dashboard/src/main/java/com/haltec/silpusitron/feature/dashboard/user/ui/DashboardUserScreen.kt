package com.haltec.silpusitron.feature.dashboard.user.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import com.haltec.silpusitron.feature.dashboard.common.ui.parts.DashboardContent
import org.koin.compose.KoinApplication


@Composable
fun DashboardUserScreen(
    modifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    state: DashboardUserUiState,
    action: (action: DashboardUserUiAction) -> Unit,
    animateWelcome: Boolean
){

    ContainerWithBanner(
        modifier = modifier
            .fillMaxSize(),
        bannerModifier = Modifier
            .height(242.dp),
        sharedModifier = sharedModifier,
        withWelcome = animateWelcome,
        onBannerInvisible = {
            println("onBannerInvisible: $it")
        }
    ) {

        DashboardContent(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 100.dp)
                .padding(horizontal = 18.dp),
            data = state.data,
            onTryAgain = {action(DashboardUserUiAction.GetData)}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview(){
    KoinApplication(application = {
        modules(listOf(commonModule, dataModule, dashboardModule))
    }) {
        val state by remember {
            mutableStateOf(
                dashboardUiStateDummy
            )
        }
        SILPUSITRONTheme {
            DashboardUserScreen(
                state = state,
                action = {action -> },
                animateWelcome = true
            )
        }
    }
}
