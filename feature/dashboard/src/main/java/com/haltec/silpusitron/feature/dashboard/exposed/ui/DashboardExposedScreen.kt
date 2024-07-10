package com.haltec.silpusitron.feature.dashboard.exposed.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.theme.gradientColors
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import com.haltec.silpusitron.feature.dashboard.common.ui.parts.DashboardContent
import com.haltec.silpusitron.feature.dashboard.exposed.domain.model.NewsImage
import com.haltec.silpusitron.feature.dashboard.exposed.ui.parts.DashboardFilterView
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.haltec.silpusitron.core.ui.R as CoreR


@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewsImagesPager(
    onDismissRequest: () -> Unit,
    data: List<NewsImage>,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = stringResource(id = CoreR.string.cancel)
                    )
                }

                val pagerState = rememberPagerState(
                    pageCount = data.size
                )
                LaunchedEffect(key1 = Unit) {
                    while (true){
                        pagerState.animateScrollToPage(
                            if (pagerState.currentPage < pagerState.pageCount-1){
                                pagerState.currentPage + 1
                            }else{
                                0
                            }
                        )

                        // Introduce a delay and check if the coroutine is still active
                        delay(5000) // Adjust the delay as needed
                        if (!isActive) break // Exit the loop if the coroutine is cancelled
                    }
                }
                HorizontalPager(state = pagerState) { page ->
                    // Our page content
                    Column {
                        Text(
                            text = data[page].title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .align(Alignment.CenterHorizontally)

                        )
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(data[page].imageURL)
                                .crossfade(true)
                                .build(),
                            placeholder = null,
                            error = painterResource(id = CoreR.drawable.error_alert),
                            onError = {
                                Log.e("NewsImage", it.result.throwable.message.toString())
                            },
                            contentDescription = data[page].title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardExposedScreen(
    modifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    state: DashboardExposedUiState,
    action: (action: DashboardExposedUiAction) -> Unit,
    onLogin: () -> Unit
){

    var showFilterSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showNewsDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        action(DashboardExposedUiAction.GetData)
        action(DashboardExposedUiAction.LoadDistricts)
        action(DashboardExposedUiAction.LoadNews)
    }

    LaunchedEffect(key1 = state.news) {
        if (state.news is Resource.Success) showNewsDialog = true
    }

    if (showNewsDialog){
        NewsImagesPager(
            onDismissRequest = {
                showNewsDialog = false
            },
            data = state.news.data ?: listOf()
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    showFilterSheet = true
                }
            ) {
                Icon(Icons.Filled.FilterAlt, contentDescription = "")
            }
        }
    ) {contentPadding ->


        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            gradientColors,
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 1000f),
                        ),
                        shape = RoundedCornerShape(
                            bottomEnd = 20.dp, bottomStart = 20.dp
                        )
                    )
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp)

            ){
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
                    .padding(horizontal = 18.dp)
                ,
                data = state.data,
                onTryAgain = {action(DashboardExposedUiAction.GetData)}
            )

        }

        // bottom sheet
        if (showFilterSheet){
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
                        action(DashboardExposedUiAction.SetFilter(districtId, startDate, endDate))
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
                onLogin = {}
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
                onLogin = {}
            )
        }
    }
}
