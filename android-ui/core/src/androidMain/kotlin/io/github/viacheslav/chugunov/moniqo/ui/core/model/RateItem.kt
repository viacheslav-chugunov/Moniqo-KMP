package io.github.viacheslav.chugunov.moniqo.ui.core.model

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo

data class RateItem(
    val currency: CurrencyInfo,
    val rate: String,
)
