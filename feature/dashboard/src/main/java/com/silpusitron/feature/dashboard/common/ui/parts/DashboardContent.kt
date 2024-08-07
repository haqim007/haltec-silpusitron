package com.silpusitron.feature.dashboard.common.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.silpusitron.core.ui.parts.error.ErrorView
import com.silpusitron.core.ui.parts.loading.LoadingView
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.silpusitron.core.ui.R as CoreR

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    chartModifier: Modifier = Modifier,
    data: Resource<List<DashboardData>>,
    onTryAgain: () -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {

        when(data){
            is Resource.Success -> {
                DashboardChartsView(
                    modifier = chartModifier,
                    data = data.data ?: emptyList(),
                    onTryAgain = onTryAgain
                )
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