package io.github.viacheslav.chugunov.moniqo.storage.model

import io.github.viacheslav.chugunov.moniqo.storage.db.CurrencyRatesEntity
import io.github.viacheslav.chugunov.moniqo.storage.db.RateEntity

internal data class CurrencyRatesRecord(
    val entity: CurrencyRatesEntity,
    val rates: List<RateEntity>,
)
