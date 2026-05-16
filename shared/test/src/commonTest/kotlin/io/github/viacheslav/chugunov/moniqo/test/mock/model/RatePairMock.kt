package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair

val ratePairMock =
    RatePair(
        fromRate = Rate(Currency.of("usd"), 1.1),
        toRate = Rate(Currency.of("gbp"), 0.85),
        baseCurrency = Currency.of("eur"),
        updatedAt = "2024-01-01",
    )
