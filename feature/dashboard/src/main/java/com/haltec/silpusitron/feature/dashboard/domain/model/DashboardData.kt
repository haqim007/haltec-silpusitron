package com.haltec.silpusitron.feature.dashboard.domain.model

import co.yml.charts.ui.piechart.models.PieChartData

data class DashboardData(
    val summaries: List<Summary>,
    val pieData: List<PieData> = listOf()
){
    data class Summary(
        val label: String,
        val value: Int
    )

    data class PieData(
        val label: String,
        val value: Float
    )
}
