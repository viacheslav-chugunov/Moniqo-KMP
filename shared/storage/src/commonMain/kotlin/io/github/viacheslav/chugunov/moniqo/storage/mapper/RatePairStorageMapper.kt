package io.github.viacheslav.chugunov.moniqo.storage.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.storage.db.RatePairEntity

interface RatePairStorageMapper {
    fun toDomain(entity: RatePairEntity): RatePair
}

internal class RatePairStorageMapperImpl : RatePairStorageMapper {
    override fun toDomain(entity: RatePairEntity): RatePair =
        RatePair(
            fromRate = Rate(currency = Currency.of(entity.from_currency), rate = entity.from_rate),
            toRate = Rate(currency = Currency.of(entity.to_currency), rate = entity.to_rate),
            baseCurrency = Currency.of(entity.base_currency),
            updatedAt = entity.updated_at,
        )
}
