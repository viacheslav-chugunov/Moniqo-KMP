package io.github.viacheslav.chugunov.moniqo.test.mock.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.storage.db.CurrencyRatesEntity
import io.github.viacheslav.chugunov.moniqo.storage.db.RateEntity
import io.github.viacheslav.chugunov.moniqo.storage.mapper.CurrencyStorageMapper
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesMock

class CurrencyStorageMapperMock(
    private val result: CurrencyRates = currencyRatesMock,
) : CurrencyStorageMapper {
    var lastEntity: CurrencyRatesEntity? = null
    var lastRates: List<RateEntity>? = null

    override fun toDomain(
        entity: CurrencyRatesEntity,
        rates: List<RateEntity>,
    ): CurrencyRates {
        lastEntity = entity
        lastRates = rates
        return result
    }
}
