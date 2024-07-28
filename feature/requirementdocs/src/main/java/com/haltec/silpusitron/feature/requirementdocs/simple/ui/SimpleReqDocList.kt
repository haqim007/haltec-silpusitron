package com.haltec.silpusitron.feature.requirementdocs.simple.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.parts.SmallTopBar
import com.haltec.silpusitron.core.ui.parts.PagerView
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.feature.requirementdocs.R
import com.haltec.silpusitron.feature.requirementdocs.common.di.requirementDocModule
import com.haltec.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import com.haltec.silpusitron.feature.requirementdocs.common.domain.requirementDocDummies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.haltec.silpusitron.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleReqDocList(
    modifier: Modifier = Modifier,
    data: Flow<PagingData<RequirementDoc>>,
    action: (action: SimpleReqDocUiAction) -> Unit,
    onClose: () -> Unit
){

    val pagingItems: LazyPagingItems<RequirementDoc> =
        data.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
        action(SimpleReqDocUiAction.LoadData)
    }

    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        SmallTopBar{
            Text(
                text = stringResource(R.string.requirement_docs_info),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

            IconButton(
                colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .size(20.dp),
                onClick = onClose
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = CoreR.string.close)
                )
            }
        }

        PagerView(
            modifier = modifier,
            pagingItems = pagingItems,
            onLoadData = {
                action(SimpleReqDocUiAction.LoadData)
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

                    var open by remember {
                        mutableStateOf(false)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 24.dp),
                    ) {
                        Surface(
                            shadowElevation = 10.dp,
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Row(
                                modifier = Modifier.clickable {
                                    open = !open
                                }
                            ) {
                                Text(
                                    text = doc.title,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .wrapContentHeight()
                                        .padding(16.dp)
                                        .weight(2f),
                                )
                                IconButton(
                                    onClick = { open = !open }
                                ) {
                                    Icon(
                                        imageVector = if (!open) Icons.Filled.KeyboardArrowDown
                                        else Icons.Filled.KeyboardArrowUp,
                                        contentDescription = stringResource(id = CoreR.string.open)
                                    )
                                }
                            }
                        }


                        AnimatedVisibility(
                            visible = open,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors().copy(
                                    containerColor = MaterialTheme.colorScheme.background
                                ),
                                border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(
                                    bottomEnd = 16.dp,
                                    bottomStart = 16.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Surat ${doc.letterType} tingkat ${doc.letterLevel}",
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        fontSize = 15.sp
                                    )

                                    Text(
                                        text = stringResource(R.string.requirements),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                    doc.requirementDocs.forEach {
                                        Text(
                                            text = "- $it",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SimpleReqDocList_Preview(){
    KoinPreviewWrapper(modules = listOf(commonModule, dataModule, requirementDocModule)) {
        SILPUSITRONTheme {
            SimpleReqDocList(
                data = flowOf(PagingData.from(requirementDocDummies)),
                action = {_ ->},
                onClose = {}
            )
        }
    }
}