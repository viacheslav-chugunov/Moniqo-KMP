package io.github.viacheslav.chugunov.moniqo.test.mock.datasource

import io.github.viacheslav.chugunov.moniqo.network.datasource.CurrencyRemoteDataSource
import io.github.viacheslav.chugunov.moniqo.network.dto.CurrencyRatesDto
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesDtoMock

class CurrencyRemoteDataSourceMock(
    private val dto: CurrencyRatesDto = currencyRatesDtoMock,
) : CurrencyRemoteDataSource {
    override suspend fun getEurRates() = dto
}
