package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair

val ratePairFallbackMock =
    RatePair(
        fromRate = Rate(Currency.of("usd"), 1.0),
        toRate = Rate(Currency.of("eur"), 1.0),
        baseCurrency = Currency.of("eur"),
        updatedAt = "2023-01-01",
    )
