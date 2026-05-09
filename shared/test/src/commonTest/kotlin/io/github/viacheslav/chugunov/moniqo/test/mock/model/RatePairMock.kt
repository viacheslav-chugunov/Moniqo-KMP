package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair

val ratePairMock =
    RatePair(
        fromRate = Rate("usd", 1.1),
        toRate = Rate("gbp", 0.85),
        baseCurrency = "eur",
        updatedAt = "2024-01-01",
    )
