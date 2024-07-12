package com.haltec.silpusitron.feature.dashboard.exposed.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Settings
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
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.IntroShowcaseState
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.component.SmallTopBar
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.R
import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import com.haltec.silpusitron.feature.dashboard.common.ui.parts.DashboardContent
import com.haltec.silpusitron.feature.dashboard.exposed.ui.parts.DashboardFilterView
import com.haltec.silpusitron.feature.dashboard.exposed.ui.parts.NewsImagesPager
import kotlinx.coroutines.delay
import com.haltec.silpusitron.core.ui.R as CoreR


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
        mutableStateOf(false)
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

    val introShowcaseState = rememberIntroShowcaseState()


    Scaffold(
        floatingActionButton = {
            DashboardFloatingButton(
                state,
                action,
                introShowcaseState,
                showAllFloatingButton,
                onShowAllFloatingButton = {show ->
                    showAllFloatingButton = show
                },
                onOpenRequirementFiles,
                showFilterSheet,
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
    introShowcaseState: IntroShowcaseState,
    showAllFloatingButton: Boolean,
    onShowAllFloatingButton: (show: Boolean) -> Unit,
    onOpenRequirementFiles: () -> Unit,
    showFilterSheet: Boolean,
    onShowFilterSheet: (show: Boolean) -> Unit,
) {
    IntroShowcase(
        showIntroShowCase = state.showAppIntro,
        dismissOnClickOutside = true,
        onShowCaseCompleted = {
            //App Intro finished!!
            action(DashboardExposedUiAction.ShowAppIntro(false))
        },
        state = introShowcaseState,
    ) {

        AnimatedVisibility(
            visible = showAllFloatingButton,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 500)
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
                            index = 2,
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
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = CoreR.string.close)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !showAllFloatingButton,
            enter = expandIn(),
            exit = shrinkOut()
        ){
            Column{
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        onShowAllFloatingButton(true)
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
                                        text = stringResource(R.string.more_menu),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = stringResource(CoreR.string.click_to_see_more_options),
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
                    Icon(
                        imageVector = Icons.Filled.MoreHoriz,
                        contentDescription = stringResource(id = CoreR.string.more)
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DashboardExposedScreenPreview(){
    KoinPreviewWrapper(
        modules = listOf(commonModule, dataModule, dashboardModule)
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
        modules = listOf(commonModule, dataModule, dashboardModule)
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
