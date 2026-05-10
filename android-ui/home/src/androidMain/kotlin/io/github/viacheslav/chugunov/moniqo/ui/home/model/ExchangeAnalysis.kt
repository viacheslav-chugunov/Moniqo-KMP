package io.github.viacheslav.chugunov.moniqo.ui.home.model

internal data class ExchangeAnalysis(
    val officialRate: String,
    val enteredRate: String,
    val differencePercent: String,
    val lossOrProfitLabel: String,
    val lossOrProfitAmount: String,
    val quality: DealQuality,
)
