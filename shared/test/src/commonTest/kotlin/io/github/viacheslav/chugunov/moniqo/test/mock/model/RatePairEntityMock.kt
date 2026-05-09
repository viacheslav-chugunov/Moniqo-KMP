package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.storage.db.RatePairEntity

val ratePairEntityMock =
    RatePairEntity(
        from_currency = "usd",
        from_rate = 1.1,
        to_currency = "gbp",
        to_rate = 0.85,
        base_currency = "eur",
        updated_at = "2024-01-01",
    )
