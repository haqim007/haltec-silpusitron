package com.silpusitron.core.ui.parts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.silpusitron.core.ui.R
import com.silpusitron.core.ui.parts.error.EmptyView
import com.silpusitron.core.ui.parts.error.ErrorView
import com.silpusitron.core.ui.parts.loading.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> PagerView(
    modifier: Modifier,
    pagingItems: LazyPagingItems<T>,
    onLoadData: () -> Unit,
    onResetFilter: (() -> Unit)? = null,
    emptyDataMessage: String = stringResource(R.string.there_is_no_data),
    content: @Composable (modifier: Modifier) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    if(pullToRefreshState.isRefreshing){
        pagingItems.refresh()
    }

    LaunchedEffect(key1 = Unit) {
        if (
            pagingItems.itemCount > 0 &&
            (pagingItems.loadState.refresh != LoadState.Loading ||
                    pagingItems.loadState.append != LoadState.Loading)
        ){
            pagingItems.refresh()
        }
    }

    LaunchedEffect(pagingItems.loadState) {
        when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> Unit
            is LoadState.Error, is LoadState.NotLoading -> {
                pullToRefreshState.endRefresh()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        if (pagingItems.loadState.refresh is LoadState.Loading) {
            if (pagingItems.itemCount < 1) {
                LoadingView()
            }
        } else if (pagingItems.loadState.refresh is LoadState.Error) {
            ErrorView(
                message = stringResource(id = R.string.failed_to_load_data),
                onTryAgain = onLoadData
            )
        } else if (
            pagingItems.loadState.refresh is LoadState.NotLoading &&
            pagingItems.itemCount == 0
        ) { // empty?
            EmptyView(
                message = emptyDataMessage,
                onTryAgain = onLoadData,
                onSecondaryAction = if (onResetFilter != null){
                    {
                        TextButton(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 6.dp)
                                .wrapContentWidth(),
                            onClick = onResetFilter,
                        ) {
                            Text(
                                text = stringResource(R.string.reset_filter),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else null
            )

        }else{
            content(Modifier)
        }
    }
}