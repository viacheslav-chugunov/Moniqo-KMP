package io.github.viacheslav.chugunov.moniqo.network.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto

internal interface CurrencyRatesMapper {
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
            baseCurrency = baseCurrency,
            rates = dto.eur.map { Rate(it.key, it.value) },
        )
}
