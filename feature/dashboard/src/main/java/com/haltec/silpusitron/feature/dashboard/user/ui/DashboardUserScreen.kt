package com.haltec.silpusitron.feature.dashboard.user.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.ui.parts.DashboardContent
import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication


@Composable
fun DashboardUserScreen(
    modifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    viewModel: DashboardUserViewModel = koinViewModel(),
    animateWelcome: Boolean
){

    val state by viewModel.state.collectAsState()
    val action = { action: DashboardUserUiAction ->
        viewModel.doAction(action)
    }

    LaunchedEffect(key1 = Unit) {
        action(DashboardUserUiAction.GetData)
    }

    ContainerWithBanner(
        modifier = modifier
            .fillMaxSize(),
        bannerModifier = Modifier
            .height(242.dp),
        sharedModifier = sharedModifier,
        withWelcome = animateWelcome,
        onBannerInvisible = {
            println("onBannerInvisible: $it")
        },
        scrollable = state.data !is Resource.Loading
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
        modules(listOf(commonModule, dataModule, dashboardUserModule))
    }) {
        val viewModel: DashboardUserViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            viewModel.doAction(DashboardUserUiAction.SetDummyState(
                dashboardUiStateDummy
            ))
        }
        SILPUSITRONTheme {
            DashboardUserScreen(
                viewModel = viewModel,
                animateWelcome = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun DashboardScreen_LoadingPreview(){
    KoinPreviewWrapper(modules = listOf(commonModule, dataModule, dashboardUserModule)) {
        val viewModel: DashboardUserViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            viewModel.doAction(DashboardUserUiAction.SetDummyState(
                dashboardUiStateDummy.copy(
                    data = Resource.Loading()
                )
            ))
        }
        SILPUSITRONTheme {
            DashboardUserScreen(
                viewModel = viewModel,
                animateWelcome = true
            )
        }
    }
}
