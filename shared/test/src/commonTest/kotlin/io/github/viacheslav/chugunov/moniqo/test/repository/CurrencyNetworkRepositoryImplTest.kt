package io.github.viacheslav.chugunov.moniqo.test.repository

import io.github.viacheslav.chugunov.moniqo.network.repository.CurrencyNetworkRepositoryImpl
import io.github.viacheslav.chugunov.moniqo.test.mock.datasource.CurrencyRemoteDataSourceMock
import io.github.viacheslav.chugunov.moniqo.test.mock.mapper.CurrencyRatesMapperMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesDtoMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyNetworkRepositoryImplTest {

    @Test
    fun `getRates returns domain object from mapper`() =
        runTest {
            assertEquals(currencyRatesMock, makeRepo(mapper = CurrencyRatesMapperMock(result = currencyRatesMock)).getRates())
        }

    @Test
    fun `getRates passes eur as baseCurrency to mapper`() =
        runTest {
            val mapper = CurrencyRatesMapperMock()
            makeRepo(mapper = mapper).getRates()

            assertEquals("eur", mapper.lastBaseCurrency)
        }

    @Test
    fun `getRates passes dataSource result to mapper`() =
        runTest {
            val mapper = CurrencyRatesMapperMock()
            makeRepo(dataSource = CurrencyRemoteDataSourceMock(currencyRatesDtoMock), mapper = mapper).getRates()

            assertEquals(currencyRatesDtoMock, mapper.lastDto)
        }

    private fun makeRepo(
        dataSource: CurrencyRemoteDataSourceMock = CurrencyRemoteDataSourceMock(),
        mapper: CurrencyRatesMapperMock = CurrencyRatesMapperMock(),
    ) = CurrencyNetworkRepositoryImpl(dataSource = dataSource, mapper = mapper)
}
