package io.github.viacheslav.chugunov.moniqo.test.mock.mapper

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto
import io.github.viacheslav.chugunov.moniqo.network.mapper.CurrencyRatesMapper
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesMock

class CurrencyRatesMapperMock(
    private val result: CurrencyRates = currencyRatesMock,
) : CurrencyRatesMapper {
    var lastBaseCurrency: String? = null
    var lastDto: CurrencyRatesDto? = null

    override fun toDomain(
        baseCurrency: String,
        dto: CurrencyRatesDto,
    ): CurrencyRates {
        lastBaseCurrency = baseCurrency
        lastDto = dto
        return result
    }
}
