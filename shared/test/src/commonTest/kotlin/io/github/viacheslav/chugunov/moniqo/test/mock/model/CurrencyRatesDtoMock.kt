package io.github.viacheslav.chugunov.moniqo.test.mock.model

import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto

val currencyRatesDtoMock =
    CurrencyRatesDto(
        date = "2024-01-02",
        eur = mapOf("usd" to 1.1, "gbp" to 0.85),
    )
