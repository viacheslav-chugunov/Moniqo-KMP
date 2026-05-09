package io.github.viacheslav.chugunov.moniqo.network.repository

import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.network.datasource.CurrencyRemoteDataSource
import io.github.viacheslav.chugunov.moniqo.network.mapper.CurrencyRatesMapper
import kotlinx.coroutines.withContext

internal class CurrencyNetworkRepositoryImpl(
    private val dataSource: CurrencyRemoteDataSource,
    private val mapper: CurrencyRatesMapper,
    private val dispatchers: CoroutineDispatchers,
) : CurrencyNetworkRepository {
    override suspend fun getRates(): CurrencyRates = withContext(dispatchers.io) {
        mapper.toDomain(baseCurrency = "eur", dto = dataSource.getEurRates())
    }
}
