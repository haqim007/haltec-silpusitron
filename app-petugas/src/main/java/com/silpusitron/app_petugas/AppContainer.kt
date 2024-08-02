package com.silpusitron.app_petugas

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.silpusitron.app_petugas.ui.nav.HomeRoute
import com.silpusitron.app_petugas.ui.nav.LoginRoute
import com.silpusitron.app_petugas.ui.nav.OTPRoute
import com.silpusitron.app_petugas.ui.nav.PublicDashboardRoute
import com.silpusitron.app_petugas.ui.nav.SimpleRequirementFilesRoute
import com.silpusitron.app_petugas.ui.nav.SplashScreenRoute
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.feature.auth.common.domain.UserType
import com.silpusitron.feature.auth.login.ui.LoginScreen
import com.silpusitron.feature.auth.otp.ui.OTPScreen
import com.silpusitron.feature.dashboard.exposed.ui.DashboardExposedScreen
import com.silpusitron.feature.dashboard.exposed.ui.DashboardExposedViewModel
import com.silpusitron.feature.landingpage.ui.splash.MySplashScreen
import com.silpusitron.homeofficer.HomeOfficerScreen
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContainer(
    viewModel: AppViewModel = koinViewModel()
){
    val navController = rememberNavController()
    val state = viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.value.isSessionValid) {
        if(state.value.isSessionValid == true){
            navController.navigate(HomeRoute){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
        }else if(state.value.isSessionValid == false){
            navController.navigate(PublicDashboardRoute){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
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
                            }
                        )
                    }
                    composable<LoginRoute> {
                        LoginScreen(
                            userType = UserType.OFFICER,
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
                            onProfileDataIncomplete = {}
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
                    composable<OTPRoute> {
                        OTPScreen()
                    }
                    composable<HomeRoute> {
                        HomeOfficerScreen(
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