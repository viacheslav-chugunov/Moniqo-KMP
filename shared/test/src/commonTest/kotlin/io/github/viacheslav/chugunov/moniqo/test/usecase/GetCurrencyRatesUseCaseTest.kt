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
    fun `returns network rates on success`() =
        runTest {
            val useCase =
                GetCurrencyRatesUseCase(
                    storageRepository = CurrencyStorageRepositoryMock(),
                    networkRepository = CurrencyNetworkRepositoryMock(),
                )

            assertEquals(currencyRatesMock, useCase())
        }

    @Test
    fun `saves network rates to storage on success`() =
        runTest {
            val storageRepo = CurrencyStorageRepositoryMock()
            GetCurrencyRatesUseCase(storageRepo, CurrencyNetworkRepositoryMock())()

            assertEquals(currencyRatesMock, storageRepo.savedRates)
        }

    @Test
    fun `falls back to storage when network throws`() =
        runTest {
            val useCase =
                GetCurrencyRatesUseCase(
                    storageRepository = CurrencyStorageRepositoryMock(),
                    networkRepository = CurrencyNetworkRepositoryMock(shouldThrow = true),
                )

            assertEquals(currencyRatesFallbackMock, useCase())
        }

    @Test
    fun `does not save when network throws`() =
        runTest {
            val storageRepo = CurrencyStorageRepositoryMock()
            GetCurrencyRatesUseCase(storageRepo, CurrencyNetworkRepositoryMock(shouldThrow = true))()

            assertNull(storageRepo.savedRates)
        }
}
