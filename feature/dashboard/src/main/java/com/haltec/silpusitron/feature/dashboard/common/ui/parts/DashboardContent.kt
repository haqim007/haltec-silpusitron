package com.haltec.silpusitron.feature.dashboard.common.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.haltec.silpusitron.core.ui.R as CoreR
import com.haltec.silpusitron.core.ui.parts.ErrorView
import com.haltec.silpusitron.core.ui.parts.LoadingView
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserUiAction
import com.haltec.silpusitron.feature.dashboard.user.ui.DashboardUserUiState

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    data: Resource<List<DashboardData>>,
    onTryAgain: () -> Unit
){
    Column(
        modifier = modifier
    ) {

        when(data){
            is Resource.Success -> {
                DashboardChartsView(data.data ?: emptyList())
            }
            is Resource.Error -> {
                ErrorView(
                    message = data.message ?:
                    stringResource(id = CoreR.string.unknown_error),
                    onTryAgain = onTryAgain
                )
            }
            else -> {
                LoadingView()
            }
        }

    }
}