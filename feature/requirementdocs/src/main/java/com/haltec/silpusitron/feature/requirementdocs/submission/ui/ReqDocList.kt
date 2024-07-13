package com.haltec.silpusitron.feature.requirementdocs.submission.ui

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haltec.silpusitron.core.ui.component.SmallTopBar
import com.haltec.silpusitron.core.ui.parts.AppExposedDropdown
import com.haltec.silpusitron.core.ui.parts.AppExposedDropdownModel
import com.haltec.silpusitron.core.ui.parts.PagerView
import com.haltec.silpusitron.core.ui.parts.getAppTextFieldColors
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.theme.gradientColors
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.requirementdocs.R
import com.haltec.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.haltec.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import com.haltec.silpusitron.feature.requirementdocs.common.domain.requirementDocDummies
import com.haltec.silpusitron.feature.requirementdocs.submission.ui.parts.ReqDocView
import com.haltec.silpusitron.shared.form.domain.model.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.haltec.silpusitron.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReqDocList(
    modifier: Modifier = Modifier,
    data: Flow<PagingData<RequirementDoc>>,
    state: ReqDocUiState,
    action: (action: ReqDocUiAction) -> Unit,
){

    val pagingItems: LazyPagingItems<RequirementDoc> =
        data.collectAsLazyPagingItems()

    var showFilterSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(key1 = Unit) {
        action(ReqDocUiAction.LoadLetterLevel)
        action(ReqDocUiAction.LoadLetterType)
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
                text = stringResource(CoreR.string.doc_submission_title),
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
                .background(
                    brush = Brush.linearGradient(
                        gradientColors,
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 1000f),
                    )
                )
                .fillMaxWidth()
                .height(50.dp))

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
                        placeholder = { Text(text = stringResource(id = CoreR.string.search))},
                        onValueChange = {
                            action(ReqDocUiAction.Search(it))
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
                                keyboardController?.hide()
                                focusManager.clearFocus()
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
                action(ReqDocUiAction.LoadData)
            }
        ){
            LazyColumn(
                contentPadding = PaddingValues(all = 12.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }
                ) { index ->
                    val doc = pagingItems[index] ?: return@items

                    ReqDocView(
                        data = doc,
                        modifier = Modifier.padding(
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
                AppExposedDropdown(
                    value = state.filterLetterTypeId.toString(),
                    onSelect = {
                        action(ReqDocUiAction.SetFilterLetterType(it.value))
                    },
                    options = state.letterTypeOptions.data?.options?.map {
                        AppExposedDropdownModel(it.label, it.value)
                    } ?: listOf(),
                    label = {
                        Text(text = stringResource(R.string.letter_type_title))
                    },
                    trailingIcon = { setExpand, defaultIcon ->
                        when(state.letterTypeOptions){
                            is Resource.Error -> {
                                IconButton(
                                    onClick = {
                                        action(ReqDocUiAction.LoadLetterType)
                                        setExpand(false) // prevent dropdown from opening
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(com.haltec.silpusitron.core.ui.R.string.click_to_reload_this_options)
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

                AppExposedDropdown(
                    value = state.filterLetterLevel.toString(),
                    onSelect = {
                        action(ReqDocUiAction.SetFilterLetterLevel(it.value))
                    },
                    options = state.letterLevelOptions.data?.options?.map {
                        AppExposedDropdownModel(it.label, it.value)
                    } ?: listOf(),
                    label = {
                        Text(text = stringResource(R.string.letter_level_type))
                    },
                    trailingIcon = { setExpand, defaultIcon ->
                        when(state.letterLevelOptions){
                            is Resource.Error -> {
                                IconButton(
                                    onClick = {
                                        action(ReqDocUiAction.LoadLetterLevel)
                                        setExpand(false) // prevent dropdown from opening
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(com.haltec.silpusitron.core.ui.R.string.click_to_reload_this_options)
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
                        action(ReqDocUiAction.Filter)
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

                if (state.filterLetterLevel != null || state.filterLetterTypeId != null){
                    Button(
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 6.dp)
                            .fillMaxWidth()
                        ,
                        onClick = {
                            action(ReqDocUiAction.ResetFilter)
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
fun ReqDocList_Preview() {
    KoinPreviewWrapper(modules = listOf(requirementDocModule)) {
        SILPUSITRONTheme {
            ReqDocList(
                action = {},
                state = ReqDocUiState(),
                data = flowOf(
                    PagingData.from(requirementDocDummies)
                )
            )
        }
    }
}