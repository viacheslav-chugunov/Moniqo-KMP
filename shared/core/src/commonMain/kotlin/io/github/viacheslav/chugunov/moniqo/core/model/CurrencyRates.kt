package io.github.viacheslav.chugunov.moniqo.core.model

data class CurrencyRates(
    val updatedAt: String,
    val baseCurrency: String,
    val rates: List<Rate>
)
