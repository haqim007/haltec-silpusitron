package com.silpusitron.app

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.feature.auth.common.domain.UserType
import com.silpusitron.feature.auth.login.ui.LoginScreen
import com.silpusitron.feature.auth.otp.ui.OTPScreen
import com.silpusitron.feature.dashboard.exposed.ui.DashboardExposedScreen
import com.silpusitron.feature.dashboard.exposed.ui.DashboardExposedViewModel
import com.silpusitron.feature.landingpage.ui.splash.MySplashScreen
import com.silpusitron.feature.requirementdocs.simple.ui.SimpleReqDocList
import com.silpusitron.feature.requirementdocs.simple.ui.SimpleReqDocViewModel
import com.silpusitron.home.HomeScreen
import com.haltec.silpusitron.ui.nav.ConfirmProfileDataRoute
import com.haltec.silpusitron.ui.nav.HomeRoute
import com.haltec.silpusitron.ui.nav.LoginRoute
import com.haltec.silpusitron.ui.nav.OTPRoute
import com.haltec.silpusitron.ui.nav.PublicDashboardRoute
import com.haltec.silpusitron.ui.nav.SimpleRequirementFilesRoute
import com.haltec.silpusitron.ui.nav.SplashScreenRoute
import com.silpusitron.feature.confirmprofilecitizen.ui.ConfirmProfileCitizenScreen
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContainer(
    viewModel: AppViewModel = koinViewModel()
){
    val navController = rememberNavController()
    val state = viewModel.state.collectAsState()
    var splashscreenHasShown by rememberSaveable {
        mutableStateOf(false)
    }
    var homeRouteHasShown by rememberSaveable {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        // Do something with your state
        // You may want to use DisposableEffect or other alternatives
        // instead of LaunchedEffect
        if (lifecycleState == Lifecycle.State.RESUMED && splashscreenHasShown){
            viewModel.doAction(AppUiAction.CheckSession)
        }
    }

    LaunchedEffect(key1 = state.value.isSessionValid) {
        if(state.value.isSessionValid == true && !homeRouteHasShown){
            navController.navigate(HomeRoute){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
            homeRouteHasShown = true // prevent to be shown again after on resume triggered
        }else if(state.value.isSessionValid == false){
            navController.navigate(PublicDashboardRoute){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
            homeRouteHasShown = false
        }
    }

    SILPUSITRONTheme {
        // A surface container using the 'background' color from the theme
        SharedTransitionLayout {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                NavHost(navController = navController, startDestination = SplashScreenRoute) {
                    composable<SplashScreenRoute> {
                        MySplashScreen(
                            sharedModifier = Modifier.sharedElement(
                                this@SharedTransitionLayout.rememberSharedContentState(key = "logo"),
                                animatedVisibilityScope = this@composable
                            ),
                            navigate = {
                                viewModel.doAction(AppUiAction.CheckSession)
                                splashscreenHasShown = true
                            }
                        )
                    }
                    composable<LoginRoute> {
                        LoginScreen(
                            userType = UserType.CITIZEN,
                            sharedModifier = Modifier
                                .sharedElement(
                                    this@SharedTransitionLayout.rememberSharedContentState(key = "logo"),
                                    animatedVisibilityScope = this@composable
                                ),
                            onProfileDataComplete = {
                                navController.navigate(OTPRoute) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            },
                            onProfileDataIncomplete = {
                                navController.navigate(ConfirmProfileDataRoute) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<PublicDashboardRoute> {
                        val dashboardViewModel: DashboardExposedViewModel = koinViewModel()
                        val dashboardState by dashboardViewModel.state.collectAsState()

                        DashboardExposedScreen(
                            state = dashboardState,
                            action = dashboardViewModel::doAction,
                            onOpenRequirementFiles = {
                                navController.navigate(SimpleRequirementFilesRoute)
                            },
                            onLogin = {
                                navController.navigate(LoginRoute) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<SimpleRequirementFilesRoute> {
                        val reqDocsViewModel: SimpleReqDocViewModel = koinViewModel()
                        //val reqDocsState by reqDocsViewModel.state.collectAsState()

                        SimpleReqDocList(
                            data = reqDocsViewModel.pagingFlow,
                            action = {action -> reqDocsViewModel.doAction(action)},
                            onClose = {
                                navController.navigateUp()
                            }
                        )
                    }
                    composable<ConfirmProfileDataRoute> {
                        ConfirmProfileCitizenScreen(
                            onTokenExpired = {
                                navController.navigate(LoginRoute) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            },
                            onComplete = {
                                navController.navigate(OTPRoute) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<OTPRoute> {
                        OTPScreen()
                    }
                    composable<HomeRoute> {
                        HomeScreen(
                            sharedModifier = Modifier
                                .sharedElement(
                                    this@SharedTransitionLayout.rememberSharedContentState(key = "logo"),
                                    animatedVisibilityScope = this@composable
                                )
                        )
                    }
                }
            }
        }
    }
}