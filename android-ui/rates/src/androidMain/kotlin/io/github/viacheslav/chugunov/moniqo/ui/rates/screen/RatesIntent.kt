package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter

sealed interface RatesIntent {
    data object Refresh : RatesIntent

    data class Search(val query: String) : RatesIntent

    data class Filter(val filter: CurrencyFilter) : RatesIntent
}
