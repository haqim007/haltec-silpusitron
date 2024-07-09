package com.haltec.silpusitron.feature.dashboard.common.domain.model

data class ServiceRatio(
    val label: String,
    val incomingLetter: Int,
    val outgoingLetter: Int
)

val ServiceRatioDummies = listOf(
    ServiceRatio("Desa 1", 9, 3),
    ServiceRatio("Desa 2", 91, 33),
    ServiceRatio("Desa 3", 29, 30),
    ServiceRatio("Desa 4", 19, 4),
    ServiceRatio("Desa 5", 19, 4)
)