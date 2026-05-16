package io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.screen

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyFilter
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.navigation.CurrencySlot

internal sealed interface ChooseCurrencyIntent {
    data class Search(val query: String) : ChooseCurrencyIntent

    data class Filter(val filter: CurrencyFilter) : ChooseCurrencyIntent

    data class SelectCurrency(val currency: CurrencyInfo, val slot: CurrencySlot) : ChooseCurrencyIntent
}
