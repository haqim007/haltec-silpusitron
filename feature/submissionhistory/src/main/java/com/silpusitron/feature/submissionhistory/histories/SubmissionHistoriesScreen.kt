package com.silpusitron.feature.submissionhistory.histories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haltec.silpusitron.core.ui.backgroundGradient
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.AppExposedDropdown
import com.haltec.silpusitron.core.ui.parts.AppExposedDropdownModel
import com.haltec.silpusitron.core.ui.parts.PagerView
import com.haltec.silpusitron.core.ui.parts.SmallTopBar
import com.haltec.silpusitron.core.ui.parts.getAppTextFieldColors
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submissionhistory.common.di.submissionHistoryModule
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistoryDummies
import com.silpusitron.feature.submissionhistory.histories.parts.HistoryItemView
import com.silpusitron.shared.form.ui.parts.InputDatePicker
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionHistoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: SubmissionHistoriesViewModel = koinViewModel(),
    onClick: (history: SubmissionHistory) -> Unit
){
    val state by viewModel.state.collectAsState()
    val action = {action: HistoryListUiAction -> viewModel.doAction(action)}

    val pagingItems = viewModel.pagingFlow.collectAsLazyPagingItems()

    var showFilterSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(key1 = Unit) {
        action(HistoryListUiAction.LoadLetterStatus)
        action(HistoryListUiAction.LoadLetterType)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SmallTopBar(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.height(50.dp),
        ){
            Text(
                text = stringResource(CoreR.string.history),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 100.dp)
        ){
            Box(modifier = Modifier
                .backgroundGradient()
                .fillMaxWidth()
                .height(50.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
            ){
                Surface(
                    shadowElevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(5f)
                ) {
                    TextField(
                        shape = RoundedCornerShape(8.dp),
                        value = state.searchKeyword,
                        placeholder = { Text(text = stringResource(id = CoreR.string.search)) },
                        onValueChange = {
                            action(HistoryListUiAction.Search(it))
                        },
                        colors = getAppTextFieldColors().copy(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            },
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                    )
                }

                Button(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors()
                        .copy(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.75f)
                        ),
                    modifier = Modifier
                        .weight(2f)
                        .height(55.dp)
                        .padding(start = 8.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 10.dp
                    ),
                    onClick = { showFilterSheet = !showFilterSheet }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = stringResource(id = CoreR.string.filter)
                        )
                        Text(text = "(${state.filterActive})", modifier = Modifier.padding(top = 5.dp))
                    }
                }
            }

        }
        PagerView(
            modifier = modifier,
            pagingItems = pagingItems,
            onLoadData = {
                action(HistoryListUiAction.LoadData)
            }
        ){
            LazyColumn(
                contentPadding = PaddingValues(all = 12.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.number }
                ) { index ->
                    val item = pagingItems[index] ?: return@items
                    HistoryItemView(
                        data = item,
                        onClick = onClick,
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                    )
                }
            }
        }
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
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var filterStartDate: LocalDateTime? by remember {
                    mutableStateOf(state.filterStartDate)
                }
                var filterEndDate: LocalDateTime? by remember {
                    mutableStateOf(state.filterEndDate)
                }

                InputDatePicker(
                    value = state.filterStartDate,
                    placeholder = {
                        Text(text = stringResource(CoreR.string.start_date))
                    },
                    setDate = {
                        filterStartDate = it
                    },
                    maxDate = filterEndDate
                )

                InputDatePicker(
                    value = state.filterEndDate,
                    placeholder = {
                        Text(text = stringResource(CoreR.string.end_date))
                    },
                    setDate = {
                        filterEndDate = it
                    },
                    minDate = filterStartDate
                )

                var filterLetterType by remember {
                    mutableStateOf(state.filterLetterTypeId)
                }
                AppExposedDropdown(
                    value = state.filterLetterTypeId.toString(),
                    onSelect = {
                        filterLetterType = it.value?.toInt()
                    },
                    options = state.letterTypeOptions.data?.options?.map {
                        AppExposedDropdownModel(it.label, it.value)
                    } ?: listOf(),
                    label = {
                        Text(text = stringResource(CoreR.string.letter_type_title))
                    },
                    trailingIcon = { setExpand, defaultIcon ->
                        when(state.letterTypeOptions){
                            is Resource.Error -> {
                                IconButton(
                                    onClick = {
                                        action(HistoryListUiAction.LoadLetterType)
                                        setExpand(false) // prevent dropdown from opening
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(CoreR.string.click_to_reload_this_options)
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                CircularProgressIndicator(
                                    color = DisabledInputContainer
                                )
                            }
                            else -> {
                                defaultIcon()
                            }
                        }
                    }

                )

                var filterLetterStatus by remember {
                    mutableStateOf(state.filterLetterStatus)
                }
                AppExposedDropdown(
                    value = state.filterLetterStatus.toString(),
                    onSelect = {
                        filterLetterStatus = it.value
                    },
                    options = state.letterStatusOptions.data?.options?.map {
                        AppExposedDropdownModel(it.label, it.value)
                    } ?: listOf(),
                    label = {
                        Text(text = stringResource(CoreR.string.letter_status_title))
                    },
                    trailingIcon = { setExpand, defaultIcon ->
                        when(state.letterStatusOptions){
                            is Resource.Error -> {
                                IconButton(
                                    onClick = {
                                        action(HistoryListUiAction.LoadLetterStatus)
                                        setExpand(false) // prevent dropdown from opening
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(CoreR.string.click_to_reload_this_options)
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                CircularProgressIndicator(
                                    color = DisabledInputContainer
                                )
                            }
                            else -> {
                                defaultIcon()
                            }
                        }
                    }

                )

                Button(
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 6.dp)
                        .fillMaxWidth()
                    ,
                    onClick = {
                        action(
                            HistoryListUiAction.SetFilter(
                                letterType = filterLetterType,
                                letterStatus = filterLetterStatus,
                                startDate = filterStartDate,
                                endDate = filterEndDate
                            )
                        )
                        showFilterSheet = false
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(
                        text = stringResource(CoreR.string.filter),
                        fontWeight = FontWeight.Bold
                    )
                }

                if (state.filterLetterStatus != null || state.filterLetterTypeId != null){
                    Button(
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 6.dp)
                            .fillMaxWidth(),
                        onClick = {
                            action(HistoryListUiAction.ResetFilter)
                            showFilterSheet = false
                        },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        Text(
                            text = stringResource(CoreR.string.reset),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SubmissionHistoriesScreen_Preview() {
    KoinPreviewWrapper(modules = listOf(submissionHistoryModule)) {
        SILPUSITRONTheme {
            val viewModel: SubmissionHistoriesViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    HistoryListUiAction.SetDummyPagingData(SubmissionHistoryDummies)
                )
            }
            SubmissionHistoriesScreen(viewModel = viewModel, onClick = {})
        }
    }
}