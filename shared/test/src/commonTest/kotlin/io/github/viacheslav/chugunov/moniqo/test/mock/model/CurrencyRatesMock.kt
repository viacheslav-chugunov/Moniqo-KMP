package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate

val currencyRatesMock =
    CurrencyRates(
        updatedAt = "2024-01-02",
        baseCurrency = "eur",
        rates = listOf(Rate("usd", 1.1), Rate("gbp", 0.85)),
    )
