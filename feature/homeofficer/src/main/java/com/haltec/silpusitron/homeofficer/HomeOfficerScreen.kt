package com.haltec.silpusitron.homeofficer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StackedBarChart
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.tab.TabBarItem
import com.haltec.silpusitron.core.ui.parts.tab.TabBarView
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.feature.dashboard.user.di.dashboardUserModule
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserScreen
import com.haltec.silpusitron.feature.officertask.docapproval.DocApprovalScreen
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetterType
import com.haltec.silpusitron.feature.officertask.tasks.ui.OfficerTasksScreen
import com.haltec.silpusitron.feature.settings.ui.AccountScreen
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.haltec.silpusitron.feature.submissionhistory.common.domain.SubmissionHistoryType
import com.haltec.silpusitron.feature.submissionhistory.docpreview.DocPreviewScreen
import com.haltec.silpusitron.feature.submissionhistory.histories.SubmissionHistoriesScreen
import com.haltec.silpusitron.feature.updateprofileofficer.ui.UpdateProfileOfficerScreen
import kotlin.reflect.typeOf
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun HomeOfficerScreen(
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
    val taskTab = TabBarItem(
        title = stringResource(CoreR.string.not_approved),
        selectedIcon = Icons.Filled.Checklist,
        unselectedIcon = Icons.Rounded.Checklist,
        navigate = {
            navController.navigate(Routes.Tasks)
        }
    )
    val monitorTab = TabBarItem(
        title = stringResource(CoreR.string.approved),
        selectedIcon = Icons.Filled.TaskAlt,
        unselectedIcon = Icons.Outlined.TaskAlt,
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
    val tabBarItems = listOf(dashboardTab, taskTab, monitorTab, settingsTab)

    var firstTimeLogin by remember {
        mutableStateOf(true)
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val showBottomBar by remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(Routes.DashboardRoute::class) || it.hasRoute(Routes.Tasks::class) ||
                        it.hasRoute(Routes.HistoriesRoute::class) || it.hasRoute(Routes.AccountRoute::class)
            } ?: false
        }
    }

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var shouldRefreshTask by remember {
        mutableStateOf(false)
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
            composable<Routes.Tasks>{
                OfficerTasksScreen(
                    modifier = Modifier.padding(paddingInner),
                    shouldRefresh = shouldRefreshTask,
                    onAfterRefresh = { shouldRefreshTask = false},
                    onClick = {
                        navController.navigate(Routes.DocPreviewApprovalRoute(it))
                    }
                )
            }
            composable<Routes.DocPreviewApprovalRoute>(
                typeMap = mapOf(typeOf<SubmittedLetter>() to SubmittedLetterType)
            ) { backStackEntry ->
                val task: SubmittedLetter by remember {
                    mutableStateOf(backStackEntry.toRoute<Routes.DocPreviewApprovalRoute>().task)
                }
                var fileUnavailable by remember {
                    mutableStateOf(task.fileUrl == null)
                }

                if (fileUnavailable){
                    AlertDialog(
                        onDismissRequest = {
                            fileUnavailable = false
                            navController.navigateUp()
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                fileUnavailable = false
                                navController.navigateUp()
                            }) {
                                Text(text = stringResource(id = CoreR.string.ok))
                            }
                        },
                        icon = {
                            LottieLoader(
                                jsonRaw = CoreR.raw.lottie_questioning,
                                modifier = Modifier.size(150.dp),
                                iterations = 1
                            )
                        },
                        text = {
                            Text(stringResource(id = CoreR.string.doc_is_not_ready_yet))
                        }
                    )
                }

                if (task.fileUrl == null) return@composable

                DocApprovalScreen(
                    modifier = Modifier.padding(paddingInner),
                    submittedLetter = task,
                    onNavigateBack = { shouldRefresh ->
                        navController.navigateUp()
                        shouldRefreshTask = shouldRefresh
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

                var onSubmissionNotFinish by remember {
                    mutableStateOf(history.fileUrl == null)
                }

                if (onSubmissionNotFinish){
                    AlertDialog(
                        onDismissRequest = {
                            onSubmissionNotFinish = false
                            navController.navigateUp()
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                onSubmissionNotFinish = false
                                navController.navigateUp()
                            }) {
                                Text(text = stringResource(id = CoreR.string.ok))
                            }
                        },
                        icon = {
                            LottieLoader(
                                jsonRaw = CoreR.raw.lottie_questioning,
                                modifier = Modifier.size(150.dp),
                                iterations = 1
                            )
                        },
                        text = {
                            Text(stringResource(id = CoreR.string.doc_is_not_ready_yet))
                        }
                    )
                }

                if (onSubmissionNotFinish) return@composable

                DocPreviewScreen(
                    modifier = Modifier.padding(paddingInner),
                    history = history,
                    onNavigateBack = navController::navigateUp,
                )
            }
            composable<Routes.AccountRoute>{
                AccountScreen(
                    modifier = Modifier.padding(paddingInner),
                    navigateToAccountProfileScreen = {
                        navController.navigate(Routes.ProfileAccountRoute)
                    }
                )
            }
            composable<Routes.ProfileAccountRoute>{
                UpdateProfileOfficerScreen(
                    modifier = Modifier.padding(paddingInner),
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onSuccessSubmit = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun HomeOfficerScreenPreview(){
    KoinPreviewWrapper(
        modules = listOf(dashboardUserModule)
    ) {
        SILPUSITRONTheme {
            HomeOfficerScreen(
                sharedModifier = Modifier
            )
        }
    }
}