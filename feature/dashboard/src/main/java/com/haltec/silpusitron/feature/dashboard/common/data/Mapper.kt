package com.haltec.silpusitron.feature.dashboard.common.data

import com.haltec.silpusitron.feature.dashboard.common.data.remote.response.DashboardResponse
import com.haltec.silpusitron.feature.dashboard.common.domain.model.BarCharts
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardChart
import com.haltec.silpusitron.feature.dashboard.common.domain.model.DashboardData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.PiesData
import com.haltec.silpusitron.feature.dashboard.common.domain.model.Summaries

fun DashboardResponse.toDashboardData(): List<DashboardData> {

    val res: MutableList<DashboardData> = mutableListOf()

    this.data?.statistik?.let {
        res.add(
            Summaries(
                type = DashboardChart.SUMMARY,
                data = it.map {
                    Summaries.Summary(
                        it.label,
                        it.value
                    )
                }
            )
        )
    }

    this.data?.rasioPelayanan?.let {
        res.add(
            BarCharts(
                type = DashboardChart.SERVICE_RATIO,
                data = it.map { item ->
                    BarCharts.BarChartsData(
                        item.label,
                        listOf(
                            item.jumlahMasuk.toFloat(),
                            item.jumlahKeluar.toFloat()
                        )
                    )
                }
            )
        )
    }

    this.data?.jenisSurat?.let {
        res.add(
            PiesData(
                type = DashboardChart.INCOMING_LETTER_BY_TYPE,
                data = it.map {
                    PiesData.PieData(
                        it.label,
                        it.value.toFloat()
                    )
                }
            )
        )
    }

    this.data?.statusSurat?.let {
        res.add(
            PiesData(
                type = DashboardChart.INCOMING_LETTER_BY_STATUS,
                data = it.map {
                    PiesData.PieData(
                        it.label,
                        it.value.toFloat()
                    )
                }
            )
        )
    }


    return res.toList()
}