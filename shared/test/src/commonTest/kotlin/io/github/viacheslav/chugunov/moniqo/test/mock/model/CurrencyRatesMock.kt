package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate

val currencyRatesMock =
    CurrencyRates(
        updatedAt = "2024-01-02",
        baseCurrency = Currency.of("eur"),
        rates = listOf(Rate(Currency.of("usd"), 1.1), Rate(Currency.of("gbp"), 0.85)),
    )
