package com.haltec.silpusitron

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
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.feature.auth.login.ui.LoginScreen
import com.haltec.silpusitron.feature.auth.otp.ui.OTPScreen
import com.haltec.silpusitron.feature.landingpage.ui.splash.MySplashScreen
import com.haltec.silpusitron.home.HomeScreen
import com.haltec.silpusitron.ui.nav.ConfirmProfileData
import com.haltec.silpusitron.ui.nav.HomeRoute
import com.haltec.silpusitron.ui.nav.LoginRoute
import com.haltec.silpusitron.ui.nav.OTP
import com.haltec.silpusitron.ui.nav.SplashScreenRoute
import com.haltec.silpusitron.user.profile.ui.ProfileDataViewModel
import com.haltec.silpusitron.user.profile.ui.ProfileDataScreen
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
            navController.navigate(LoginRoute){
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
                                this@SharedTransitionLayout.rememberSharedContentState(key = "logo-splash"),
                                animatedVisibilityScope = this@composable
                            ),
                            navigate = {
                                viewModel.doAction(AppUiAction.CheckSession)
                            }
                        )
                    }
                    composable<LoginRoute> {
                        LoginScreen(
                            userType = UserType.APP,
                            sharedModifier = Modifier
                                .sharedElement(
                                    this@SharedTransitionLayout.rememberSharedContentState(key = "logo-splash"),
                                    animatedVisibilityScope = this@composable
                                ),
                            onProfileDataComplete = {
                                navController.navigate(OTP) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            },
                            onProfileDataIncomplete = {
                                navController.navigate(ConfirmProfileData) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<ConfirmProfileData> {
                        val profileViewModel: ProfileDataViewModel = koinViewModel()
                        val profileState by profileViewModel.state.collectAsState()

                        ProfileDataScreen(
                            state = profileState,
                            action = {
                                profileViewModel.doAction(it)
                            },
                            onComplete = {
                                navController.navigate(OTP) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<OTP> {
                        OTPScreen()
                    }
                    composable<HomeRoute> {
                        HomeScreen()
                    }
                }
            }
        }
    }
}