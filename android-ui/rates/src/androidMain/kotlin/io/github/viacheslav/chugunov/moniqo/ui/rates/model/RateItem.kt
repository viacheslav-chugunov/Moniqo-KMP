package io.github.viacheslav.chugunov.moniqo.ui.rates.model

import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo

data class RateItem(
    val currency: CurrencyInfo,
    val rate: String,
)
