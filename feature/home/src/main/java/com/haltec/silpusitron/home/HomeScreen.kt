package com.haltec.silpusitron.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StackedBarChart
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.haltec.silpusitron.core.ui.parts.tab.TabBarItem
import com.haltec.silpusitron.core.ui.parts.tab.TabBarView
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserScreen
import com.haltec.silpusitron.feature.requirementdocs.submission.ui.ReqDocList
import com.haltec.silpusitron.feature.requirementdocs.submission.ui.ReqDocViewModel
import com.haltec.silpusitron.feature.settings.ui.AccountScreen
import com.haltec.silpusitron.feature.submissionhistory.docpreview.DocPreviewScreen
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocFormArgs
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocFormArgsType
import com.haltec.silpusitron.feature.submission.form.ui.SubmissionDocScreen
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistoryType
import com.haltec.silpusitron.feature.submissionhistory.histories.SubmissionHistoriesScreen
import com.haltec.silpusitron.feature.updateprofilecitizen.ui.UpdateProfileCitizenScreen
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
        title = stringResource(CoreR.string.dashboard),
        selectedIcon = Icons.Filled.StackedBarChart,
        unselectedIcon = Icons.Outlined.StackedBarChart,
        navigate = {
            navController.navigate(Routes.DashboardRoute)
        }
    )
    val addTab = TabBarItem(
        title = stringResource(CoreR.string.submission),
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Rounded.AddCircle,
        navigate = {
            navController.navigate(Routes.InquiryRoute)
        }
    )
    val historyTab = TabBarItem(
        title = stringResource(CoreR.string.history),
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        navigate = {
            navController.navigate(Routes.HistoriesRoute)
        }
    )
    val settingsTab = TabBarItem(
        title = stringResource(CoreR.string.settings),
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        navigate = {
            navController.navigate(Routes.AccountRoute)
        }
    )
    val tabBarItems = listOf(dashboardTab, addTab, historyTab, settingsTab)

    var firstTimeLogin by remember {
        mutableStateOf(true)
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val showBottomBar by remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(Routes.DashboardRoute::class) || it.hasRoute(Routes.InquiryRoute::class) ||
                        it.hasRoute(Routes.HistoriesRoute::class) || it.hasRoute(Routes.AccountRoute::class)
            } ?: false
        }
    }

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TabBarView(selectedIndex, tabItems = tabBarItems){selectedIndex = it}
            }
        }
    ) { paddingInner ->
        NavHost(navController = navController, startDestination = Routes.DashboardRoute){
            composable<Routes.DashboardRoute>{

                DisposableEffect(Unit) {
                    // Clean up resources if needed
                    // E.g., unsubscribe from listeners, close connections, etc.

                    // This lambda will be called when the composable is disposed
                    onDispose {
                        firstTimeLogin = false
                    }
                }
               DashboardUserScreen(
                   modifier = Modifier.padding(paddingInner),
                   sharedModifier = sharedModifier,
                   animateWelcome = firstTimeLogin,
               )
            }
            composable<Routes.InquiryRoute>{
                val reqDocViewModel: ReqDocViewModel = koinViewModel()
                val state by reqDocViewModel.state.collectAsState()
                ReqDocList(
                    state = state,
                    action = {action -> reqDocViewModel.doAction(action)},
                    data = reqDocViewModel.pagingFlow,
                    onClick = { args ->
                        navController.navigate(
                            Routes.FormSubmission(
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
            composable<Routes.FormSubmission>(
                typeMap = mapOf(typeOf<SubmissionDocFormArgs>() to SubmissionDocFormArgsType)
            ) { backStackEntry ->

                val param: SubmissionDocFormArgs by remember {
                    mutableStateOf(backStackEntry.toRoute<Routes.FormSubmission>().args)
                }
                SubmissionDocScreen(
                    args = param,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onSucceed = {
                        navController.navigateUp()
                    }
                )
            }
            composable<Routes.HistoriesRoute>{
                SubmissionHistoriesScreen(
                    modifier = Modifier.padding(paddingInner),
                    onClick = { history ->
                        navController.navigate(
                            Routes.DocPreviewRoute(history)
                        )
                    }
                )
            }
            composable<Routes.DocPreviewRoute>(
                typeMap = mapOf(typeOf<SubmissionHistory>() to SubmissionHistoryType)
            ) { backStackEntry ->
                val history: SubmissionHistory by remember {
                    mutableStateOf(backStackEntry.toRoute<Routes.DocPreviewRoute>().history)
                }
                DocPreviewScreen(
                    modifier = Modifier.padding(paddingInner),
                    history = history,
                    onNavigateBack = navController::navigateUp,
                )
            }
            composable<Routes.AccountRoute>{
                AccountScreen(
                    navigateToAccountProfileScreen = {
                        navController.navigate(Routes.ProfileAccountRoute)
                    }
                )
            }
            composable<Routes.ProfileAccountRoute>{
                UpdateProfileCitizenScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    KoinPreviewWrapper(
        modules = listOf(dashboardUserModule)
    ) {
        SILPUSITRONTheme {
            HomeScreen(
                sharedModifier = Modifier
            )
        }
    }
}