package io.github.viacheslav.chugunov.moniqo.ui.core.model

import io.github.viacheslav.chugunov.moniqo.core.model.DealQuality

data class ExchangeAnalysis(
    val officialRate: String,
    val enteredRate: String,
    val differencePercent: String,
    val lossOrProfitLabel: String,
    val lossOrProfitAmount: String,
    val quality: DealQuality,
)
