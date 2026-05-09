package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair

val ratePairFallbackMock =
    RatePair(
        fromRate = Rate("usd", 1.0),
        toRate = Rate("eur", 1.0),
        baseCurrency = "eur",
        updatedAt = "2023-01-01",
    )
