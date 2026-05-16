package io.github.viacheslav.chugunov.moniqo.test.mock.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesFallbackMock

class CurrencyStorageRepositoryMock(
    private val rates: CurrencyRates = currencyRatesFallbackMock,
    private val isEmpty: Boolean = false,
) : CurrencyStorageRepository {
    var savedRates: CurrencyRates? = null

    override suspend fun saveRates(rates: CurrencyRates) {
        savedRates = rates
    }

    override suspend fun getRates() = rates

    override suspend fun isEmpty() = isEmpty
}
