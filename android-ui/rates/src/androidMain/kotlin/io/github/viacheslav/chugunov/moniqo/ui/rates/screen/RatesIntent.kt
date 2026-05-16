package io.github.viacheslav.chugunov.moniqo.ui.rates.screen

sealed interface RatesIntent {
    data object Refresh : RatesIntent
    data class Search(val query: String) : RatesIntent
    data class Filter(val filter: RatesFilter) : RatesIntent
}
