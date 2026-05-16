package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.usecase.GetCurrencyRatesUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesFallbackMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesMock
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.CurrencyNetworkRepositoryMock
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.CurrencyStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetCurrencyRatesUseCaseTest {
    @Test
    fun `returns rates from storage when storage is not empty`() =
        runTest {
            val storage = CurrencyStorageRepositoryMock(currencyRatesMock, isEmpty = false)
            val network = CurrencyNetworkRepositoryMock()
            val useCase = GetCurrencyRatesUseCase(storage, network)

            assertEquals(currencyRatesMock, useCase())
            assertNull(storage.savedRates)
        }

    @Test
    fun `fetches from network and saves when storage is empty`() =
        runTest {
            val storage = CurrencyStorageRepositoryMock(currencyRatesFallbackMock, isEmpty = true)
            val network = CurrencyNetworkRepositoryMock(currencyRatesMock)
            val useCase = GetCurrencyRatesUseCase(storage, network)

            assertEquals(currencyRatesMock, useCase())
            assertEquals(currencyRatesMock, storage.savedRates)
        }

    @Test
    fun `falls back to storage when storage is empty and network fails`() =
        runTest {
            val storage = CurrencyStorageRepositoryMock(currencyRatesFallbackMock, isEmpty = true)
            val network = CurrencyNetworkRepositoryMock(shouldThrow = true)
            val useCase = GetCurrencyRatesUseCase(storage, network)

            assertEquals(currencyRatesFallbackMock, useCase())
            assertNull(storage.savedRates)
        }
}
