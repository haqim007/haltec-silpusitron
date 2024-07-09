package com.haltec.silpusitron.feature.dashboard.common.domain.model

//data class DashboardData(
//    val summaries: List<Summary>,
//    val pieData: List<PieData> = listOf()
//){
//    data class Summary(
//        val label: String,
//        val value: Int
//    )
//
//    data class PieData(
//        val label: String,
//        val value: Float
//    )
//}

enum class DashboardChart{
    SUMMARY,
    SERVICE_RATIO,
    INCOMING_LETTER_BY_TYPE,
    INCOMING_LETTER_BY_STATUS
}

sealed class DashboardData{
    abstract val type: DashboardChart
}

data class BarCharts(
    override val type: DashboardChart,
    val data: List<BarChartsData>
): DashboardData(){
    data class BarChartsData(
        val label: String,
        val bars: List<Float>,
    )
}

data class Summaries(
    override val type: DashboardChart,
    val data: List<Summary>
): DashboardData(){
    data class Summary(
        val label: String,
        val value: Int
    )
}

data class PiesData(
    override val type: DashboardChart,
    val data: List<PieData>
): DashboardData(){
    data class PieData(
        val label: String,
        val value: Float
    )
}

