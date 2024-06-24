package com.haltec.silpusitron.feature.dashboard.data

import com.haltec.silpusitron.feature.dashboard.data.remote.DashboardResponse
import com.haltec.silpusitron.feature.dashboard.domain.model.DashboardData

fun DashboardResponse.toDashboardData(): DashboardData{

    return DashboardData(
        summaries = this.data?.statistik?.map {
            DashboardData.Summary(it.label, it.value)
        } ?: listOf(),
        pieData = this.data?.jenisSurat?.map{
            DashboardData.PieData(it.label, it.value.toFloat())
        } ?: listOf()
    )
}