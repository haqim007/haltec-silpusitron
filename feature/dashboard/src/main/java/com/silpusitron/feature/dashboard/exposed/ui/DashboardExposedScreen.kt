package com.silpusitron.feature.dashboard.exposed.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import com.silpusitron.common.di.commonModule
import com.silpusitron.core.ui.parts.SmallTopBar
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.core.ui.util.KoinPreviewWrapper
import com.silpusitron.data.di.dataModule
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.R
import com.silpusitron.feature.dashboard.common.ui.parts.DashboardContent
import com.silpusitron.feature.dashboard.exposed.di.dashboardExposedModule
import com.silpusitron.feature.dashboard.exposed.ui.parts.DashboardFilterView
import com.silpusitron.feature.dashboard.common.ui.parts.NewsImagesPager
import kotlinx.coroutines.delay
import org.koin.compose.getKoin
import com.silpusitron.core.ui.R as CoreR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardExposedScreen(
    modifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    state: DashboardExposedUiState,
    action: (action: DashboardExposedUiAction) -> Unit,
    onLogin: () -> Unit,
    onOpenRequirementFiles: () -> Unit
){

    var showFilterSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showAllFloatingButton by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = Unit) {
        action(DashboardExposedUiAction.GetData)
        action(DashboardExposedUiAction.LoadDistricts)
        action(DashboardExposedUiAction.LoadNews)
    }

    LaunchedEffect(key1 = state.showNewsDialog, key2 = state.news) {
        if ((state.showNewsDialog == false && state.news is Resource.Success) || state.news is Resource.Error){
            delay(500)
            action(DashboardExposedUiAction.ShowAppIntro())
        }
        else if (state.news is Resource.Success && state.showNewsDialog != false) {
            action(DashboardExposedUiAction.ShowNewsDialog)
        }
    }

    if (state.showNewsDialog == true){
        NewsImagesPager(
            onDismissRequest = {
                action(DashboardExposedUiAction.HideNewsDialog)
            },
            data = state.news.data ?: listOf()
        )
    }


    Scaffold(
        floatingActionButton = {
            DashboardFloatingButton(
                state,
                action,
                showAllFloatingButton,
                onShowAllFloatingButton = {show ->
                    showAllFloatingButton = show
                },
                onOpenRequirementFiles,
                onShowFilterSheet = {show ->
                    showFilterSheet = show
                }
            )
        }
    ) { contentPadding ->

        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {

            SmallTopBar {
                Image(
                    painter = painterResource(id = CoreR.drawable.logo),
                    contentDescription = "logo",
                    modifier = sharedModifier
                        .height(40.dp)
                )

                Button(
                    modifier = Modifier
                        .width(90.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    onClick = onLogin
                ) {
                    Text(
                        text = stringResource(id = CoreR.string.login),
                        style = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            DashboardContent(
                chartModifier = Modifier
                    .verticalScroll(rememberScrollState()),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 18.dp),
                data = state.data,
                onTryAgain = { action(DashboardExposedUiAction.GetData) }
            )

        }

        // bottom sheet
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showFilterSheet = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                DashboardFilterView(
                    districtOptions = state.districtOptions,
                    reloadDistrict = {
                        action(DashboardExposedUiAction.LoadDistricts)
                    },
                    setFilter = { districtId, startDate, endDate ->
                        action(
                            DashboardExposedUiAction.SetFilter(
                                districtId,
                                startDate,
                                endDate
                            )
                        )
                        showFilterSheet = false
                    },
                    districtId = state.districtId,
                    startDate = state.startDate,
                    endDate = state.endDate
                )
            }
        }

    }
}

@Composable
private fun DashboardFloatingButton(
    state: DashboardExposedUiState,
    action: (action: DashboardExposedUiAction) -> Unit,
    showAllFloatingButton: Boolean,
    onShowAllFloatingButton: (show: Boolean) -> Unit,
    onOpenRequirementFiles: () -> Unit,
    onShowFilterSheet: (show: Boolean) -> Unit,
) {

    val introShowcaseState = rememberIntroShowcaseState()

    IntroShowcase(
        showIntroShowCase = state.showAppIntro,
        dismissOnClickOutside = true,
        onShowCaseCompleted = {
            //App Intro finished!!
            onShowAllFloatingButton(false)
            action(DashboardExposedUiAction.ShowAppIntro(false))
        },
        state = introShowcaseState,
    ) {

        AnimatedVisibility(
            visible = showAllFloatingButton,
            enter = slideInVertically(
                initialOffsetY = {
                    it
                }
            ),
            exit = slideOutVertically(
                targetOffsetY = {
                    it
                }
            )
        ) {
            Column {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        onOpenRequirementFiles()
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .introShowCaseTarget(
                            index = 1,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                backgroundAlpha = 0.98f, // specify transparency of background
                                targetCircleColor = MaterialTheme.colorScheme.onPrimary // specify color of target circle
                            ),
                            // specify the content to show to introduce app feature
                            content = {
                                Column {
                                    Text(
                                        text = stringResource(R.string.doc_requirements),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = stringResource(CoreR.string.click_to_see_doc_requirements),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowRightAlt,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .align(Alignment.End),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        )
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = CoreR.drawable.document_checks),
                        contentDescription = stringResource(id = CoreR.string.click_to_see_doc_requirements)
                    )
                }

                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        onShowFilterSheet(true)
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .introShowCaseTarget(
                            index = 0,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                backgroundAlpha = 0.98f, // specify transparency of background
                                targetCircleColor = MaterialTheme.colorScheme.onPrimary // specify color of target circle
                            ),
                            // specify the content to show to introduce app feature
                            content = {
                                Column {
                                    Text(
                                        text = stringResource(R.string.filter_feature),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = stringResource(R.string.click_here_to_filter_data),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowRightAlt,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .align(Alignment.End),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        )
                ) {
                    Icon(Icons.Filled.FilterAlt, contentDescription = "")
                }

                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        onShowAllFloatingButton(false)
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = CoreR.string.close)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !showAllFloatingButton,
            enter = slideInVertically(
                initialOffsetY = {
                    -it
                }
            ),
            exit = slideOutVertically(
                targetOffsetY = {
                    -it
                }
            )
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    onShowAllFloatingButton(true)
                },
                modifier = Modifier
                    .height(75.dp)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = stringResource(id = CoreR.string.more)
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun DashboardExposedScreenPreview(){
    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, dashboardExposedModule)
    ) {
        val state by remember {
            mutableStateOf(
                dashboardUiStateDummy
            )
        }
        SILPUSITRONTheme {
            DashboardExposedScreen(
                state = state,
                action = {action -> },
                onLogin = {},
                onOpenRequirementFiles = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardExposedScreenLoadingPreview(){
    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, dashboardExposedModule)
    ) {
        val state by remember {
            mutableStateOf(
                dashboardUiStateDummy.copy(
                    data = Resource.Loading()
                )
            )
        }
        SILPUSITRONTheme {
            DashboardExposedScreen(
                state = state,
                action = {action -> },
                onLogin = {},
                onOpenRequirementFiles = { }
            )
        }
    }
}
