package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate

val currencyRatesFallbackMock =
    CurrencyRates(
        updatedAt = "2024-01-01",
        baseCurrency = "eur",
        rates = listOf(Rate("usd", 1.0)),
    )
