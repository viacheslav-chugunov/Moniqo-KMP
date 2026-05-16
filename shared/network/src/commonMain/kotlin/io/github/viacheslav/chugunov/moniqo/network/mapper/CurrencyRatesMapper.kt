package io.github.viacheslav.chugunov.moniqo.network.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto

interface CurrencyRatesMapper {
    fun toDomain(
        baseCurrency: String,
        dto: CurrencyRatesDto,
    ): CurrencyRates
}

internal class CurrencyRatesMapperImpl : CurrencyRatesMapper {
    override fun toDomain(
        baseCurrency: String,
        dto: CurrencyRatesDto,
    ): CurrencyRates =
        CurrencyRates(
            updatedAt = dto.date,
            baseCurrency = Currency.of(baseCurrency),
            rates = dto.eur.map { Rate(Currency.of(it.key), it.value) },
        )
}
