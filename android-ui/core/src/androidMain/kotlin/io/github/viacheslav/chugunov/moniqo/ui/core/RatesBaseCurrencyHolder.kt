package io.github.viacheslav.chugunov.moniqo.ui.core

import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RatesBaseCurrencyHolder {
    private val _currency = MutableStateFlow<CurrencyInfo?>(null)
    val currency: StateFlow<CurrencyInfo?> = _currency

    fun select(currency: CurrencyInfo) {
        _currency.value = currency
    }
}
