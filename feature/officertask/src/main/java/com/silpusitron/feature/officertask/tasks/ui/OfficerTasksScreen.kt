package com.silpusitron.feature.officertask.tasks.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Refresh
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
import com.haltec.silpusitron.core.ui.parts.AppExposedDropdown
import com.haltec.silpusitron.core.ui.parts.AppExposedDropdownModel
import com.haltec.silpusitron.core.ui.parts.PagerView
import com.haltec.silpusitron.core.ui.parts.SmallTopBar
import com.haltec.silpusitron.core.ui.parts.getAppTextFieldColors
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.officertask.R
import com.silpusitron.feature.officertask.common.di.officerTaskModules
import com.silpusitron.feature.officertask.common.ui.DialogRejectingForm
import com.silpusitron.feature.officertask.common.ui.DialogSigningForm
import com.silpusitron.feature.officertask.common.ui.SigningResultView
import com.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.silpusitron.feature.officertask.tasks.domain.SubmittedLetterDummies
import com.silpusitron.shared.form.ui.parts.InputDatePicker
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficerTasksScreen(
    modifier: Modifier = Modifier,
    viewModel: OfficerTasksViewModel = koinViewModel(),
    onClick: (task: SubmittedLetter) -> Unit,
    shouldRefresh: Boolean,
    onAfterRefresh: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val action = {action: OfficerTasksUiAction -> viewModel.doAction(action)}

    val pagingItems = viewModel.pagingFlow.collectAsLazyPagingItems()

    var showFilterSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(key1 = Unit) {
        action(OfficerTasksUiAction.LoadLetterType)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = shouldRefresh) {
        if (shouldRefresh){
            action(OfficerTasksUiAction.LoadData)
            onAfterRefresh()
        }
    }

    var isRejecting by remember {
        mutableStateOf(false)
    }

    var isSigning by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SmallTopBar(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.height(50.dp),
        ){
            Text(
                text = stringResource(R.string.not_approved_letters),
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
                            action(OfficerTasksUiAction.Search(it))
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
            modifier = Modifier.weight(9f),
            pagingItems = pagingItems,
            onLoadData = { action(OfficerTasksUiAction.LoadData) }
        ){
            LazyColumn(
                contentPadding = PaddingValues(all = 12.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.number }
                ) { index ->
                    val item = pagingItems[index] ?: return@items
                    OfficerTaskItemView(
                        data = item,
                        onClick = {
                            onClick(it)
                        },
                        onLongClick = {
                            action(OfficerTasksUiAction.SelectId(it.id))
                        },
                        multipleSelectActive = state.selectMultipleActive,
                        onSelect = {it ->
                            action(OfficerTasksUiAction.SelectId(it.id))
                        },
                        isSelected = state.selectedIds.firstOrNull {
                            it == item.id
                        } != null,
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                    )
                }
            }
        }

        if (state.selectMultipleActive){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { isRejecting = true }
                ) {
                    Text(text = stringResource(id = CoreR.string.reject))
                }
                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { isSigning = true }
                ) {
                    Text(text = stringResource(id = CoreR.string.sign))
                }
            }
        }
    }

    // on rejecting
    if (isRejecting){
        DialogRejectingForm(
            onSubmit = { reason ->
                action(
                    OfficerTasksUiAction.Rejecting(reason)
                )
                isRejecting = false
            },
            onDismiss = {isRejecting = false}
        )
    }

    // on signing
    if (isSigning){
        DialogSigningForm(
            onDismiss = {isSigning = false},
            onSubmit = { passphrase ->
                action(OfficerTasksUiAction.Signing(passphrase))
                isSigning = false
            }
        )
    }

    // signing result
    SigningResultView(
        signingResult = state.signingResult,
        onSuccess = {
            action(OfficerTasksUiAction.LoadData)
            action(OfficerTasksUiAction.ResetSigningResult)
        },
        onDismiss = { action(OfficerTasksUiAction.ResetSigningResult) }
    )

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
                modifier = Modifier.padding(16.dp),
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
                                        action(OfficerTasksUiAction.LoadLetterType)
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
                            OfficerTasksUiAction.SetFilter(
                                letterType = filterLetterType,
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
                            action(OfficerTasksUiAction.ResetFilter)
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
fun OfficerTasksScreen_Preview() {
    KoinPreviewWrapper(modules = listOf(officerTaskModules)) {
        SILPUSITRONTheme {
            val viewModel: OfficerTasksViewModel = koinViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.doAction(
                    OfficerTasksUiAction.SetDummyPagingData(SubmittedLetterDummies)
                )
            }
            OfficerTasksScreen(
                viewModel = viewModel,
                onClick = {},
                shouldRefresh = false,
                onAfterRefresh = {}
            )
        }
    }
}