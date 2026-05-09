package io.github.viacheslav.chugunov.moniqo.storage.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.storage.db.CurrencyRatesEntity
import io.github.viacheslav.chugunov.moniqo.storage.db.RateEntity

interface CurrencyStorageMapper {
    fun toDomain(
        entity: CurrencyRatesEntity,
        rates: List<RateEntity>,
    ): CurrencyRates
}

internal class CurrencyStorageMapperImpl : CurrencyStorageMapper {
    override fun toDomain(
        entity: CurrencyRatesEntity,
        rates: List<RateEntity>,
    ): CurrencyRates =
        CurrencyRates(
            updatedAt = entity.updated_at,
            baseCurrency = entity.base_currency,
            rates = rates.map { Rate(currency = it.currency, rate = it.rate) },
        )
}
