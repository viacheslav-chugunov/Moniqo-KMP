package io.github.viacheslav.chugunov.moniqo.test.mock.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesMock

class CurrencyNetworkRepositoryMock(
    private val rates: CurrencyRates = currencyRatesMock,
    private val shouldThrow: Boolean = false,
) : CurrencyNetworkRepository {
    override suspend fun getRates(): CurrencyRates {
        if (shouldThrow) error("Network error")
        return rates
    }
}
