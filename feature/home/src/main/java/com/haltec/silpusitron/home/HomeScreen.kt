package com.haltec.silpusitron.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StackedBarChart
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.haltec.silpusitron.core.ui.component.SmallTopBar
import com.haltec.silpusitron.core.ui.parts.navigation.TabBarItem
import com.haltec.silpusitron.core.ui.parts.navigation.TabBarView
import com.haltec.silpusitron.core.ui.theme.BackgroundLight
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserScreen
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserViewModel
import com.haltec.silpusitron.feature.dashboard.user.ui.dashboardUiStateDummy
import com.haltec.silpusitron.feature.requirementdocs.submission.ui.ReqDocList
import com.haltec.silpusitron.feature.requirementdocs.submission.ui.ReqDocViewModel
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocFormArgs
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocFormArgsType
import com.haltec.silpusitron.feature.submission.ui.SubmissionDocScreen
import com.haltec.silpusitron.user.account.ui.AccountScreen
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
){
    val navController = rememberNavController()

    val dashboardTab = TabBarItem(
        title = stringResource(R.string.dashboard),
        selectedIcon = Icons.Filled.StackedBarChart,
        unselectedIcon = Icons.Outlined.StackedBarChart,
        navigate = {
            navController.navigate(DashboardRoute)
        }
    )
    val addTab = TabBarItem(
        title = stringResource(R.string.submission),
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Rounded.AddCircle,
        navigate = {
            navController.navigate(InquiryRoute)
        }
    )
    val historyTab = TabBarItem(
        title = stringResource(R.string.history),
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        navigate = {
            navController.navigate(HistoriesRoute)
        }
    )
    val profileTab = TabBarItem(
        title = stringResource(CoreR.string.account),
        selectedIcon = Icons.Filled.Person2,
        unselectedIcon = Icons.Outlined.Person,
        navigate = {
            navController.navigate(ProfileRoute)
        }
    )
    val tabBarItems = listOf(dashboardTab, addTab, historyTab, profileTab)

    var firstTimeLogin by remember {
        mutableStateOf(true)
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val showBottomBar by remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(DashboardRoute::class) || it.hasRoute(InquiryRoute::class) ||
                        it.hasRoute(HistoriesRoute::class) || it.hasRoute(ProfileRoute::class)
            } ?: false
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TabBarView(tabItems = tabBarItems)
            }
        }
    ) { paddingInner ->
        NavHost(navController = navController, startDestination = DashboardRoute){
            composable<DashboardRoute>{
                val dashboardViewModel: DashboardUserViewModel = koinViewModel()
                val state by remember {
                    mutableStateOf(dashboardUiStateDummy)
                }
                DisposableEffect(Unit) {
                    // Clean up resources if needed
                    // E.g., unsubscribe from listeners, close connections, etc.

                    // This lambda will be called when the composable is disposed
                    onDispose {
                        firstTimeLogin = false
                    }
                }
               DashboardUserScreen(
                   sharedModifier = sharedModifier,
                   state = state, /*viewModel.state.collectAsState()*/
                   animateWelcome = firstTimeLogin,
                   action = dashboardViewModel::doAction
               )
            }
            composable<InquiryRoute>{
                val reqDocViewModel: ReqDocViewModel = koinViewModel()
                val state by reqDocViewModel.state.collectAsState()
                ReqDocList(
                    state = state,
                    action = {action -> reqDocViewModel.doAction(action)},
                    data = reqDocViewModel.pagingFlow,
                    onClick = { args ->
                        navController.navigate(
                            FormSubmission(
                                SubmissionDocFormArgs(
                                    args.id,
                                    args.title,
                                    args.letterLevel,
                                    args.letterType
                                )
                            )
                        )
                    }
                )
            }

            composable<FormSubmission>(
                typeMap = mapOf(typeOf<SubmissionDocFormArgs>() to SubmissionDocFormArgsType)
            ) { backStackEntry ->

                val param: FormSubmission = backStackEntry.toRoute<FormSubmission>()
                SubmissionDocScreen(
                    args = param.args,
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable<HistoriesRoute>{
                Text(text = "Ini HistoriesRoute", modifier = Modifier.padding(paddingInner))
            }
            composable<ProfileRoute>{
                AccountScreen()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    KoinPreviewWrapper(
        modules = listOf(dashboardModule)
    ) {
        SILPUSITRONTheme {
            HomeScreen(
                sharedModifier = Modifier
            )
        }
    }
}