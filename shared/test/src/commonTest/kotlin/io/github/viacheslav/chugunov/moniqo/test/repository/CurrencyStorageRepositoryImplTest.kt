@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.github.viacheslav.chugunov.moniqo.test.repository

import io.github.viacheslav.chugunov.moniqo.storage.repository.CurrencyStorageRepositoryImpl
import io.github.viacheslav.chugunov.moniqo.test.mock.datasource.CurrencyFallbackDataSourceMock
import io.github.viacheslav.chugunov.moniqo.test.mock.datasource.CurrencyLocalDataSourceMock
import io.github.viacheslav.chugunov.moniqo.test.mock.mapper.CurrencyStorageMapperMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesEntityMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesFallbackMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.rateEntityMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyStorageRepositoryImplTest {
    @Test
    fun `saveRates delegates to localDataSource`() =
        runTest {
            val localDataSource = CurrencyLocalDataSourceMock()
            makeRepo(localDataSource = localDataSource).saveRates(currencyRatesMock)

            assertEquals(currencyRatesMock, localDataSource.savedRates)
        }

    @Test
    fun `getRates returns mapped local data when local has record`() =
        runTest {
            assertEquals(currencyRatesMock, makeRepo().getRates())
        }

    @Test
    fun `getRates returns fallback when local is empty`() =
        runTest {
            val result = makeRepo(localDataSource = CurrencyLocalDataSourceMock(record = null)).getRates()

            assertEquals(currencyRatesFallbackMock, result)
        }

    @Test
    fun `getRates passes correct entity and rates to mapper`() =
        runTest {
            val mapper = CurrencyStorageMapperMock()
            makeRepo(mapper = mapper).getRates()

            assertEquals(currencyRatesEntityMock, mapper.lastEntity)
            assertEquals(listOf(rateEntityMock), mapper.lastRates)
        }

    private fun makeRepo(
        localDataSource: CurrencyLocalDataSourceMock = CurrencyLocalDataSourceMock(),
        mapper: CurrencyStorageMapperMock = CurrencyStorageMapperMock(),
    ) = CurrencyStorageRepositoryImpl(
        localDataSource = localDataSource,
        fallbackDataSource = CurrencyFallbackDataSourceMock(),
        mapper = mapper,
    )
}
