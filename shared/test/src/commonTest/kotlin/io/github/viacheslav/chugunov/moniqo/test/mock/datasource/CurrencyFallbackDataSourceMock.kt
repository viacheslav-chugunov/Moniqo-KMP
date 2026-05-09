package io.github.viacheslav.chugunov.moniqo.test.mock.datasource

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesFallbackMock

class CurrencyFallbackDataSourceMock(
    private val rates: CurrencyRates = currencyRatesFallbackMock,
) : CurrencyFallbackDataSource {
    override suspend fun get() = rates
}
