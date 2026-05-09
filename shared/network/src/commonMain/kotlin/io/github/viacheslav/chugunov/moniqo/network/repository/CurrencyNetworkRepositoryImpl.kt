package io.github.viacheslav.chugunov.moniqo.network.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyNetworkRepository
import io.github.viacheslav.chugunov.moniqo.network.datasource.CurrencyRemoteDataSource
import io.github.viacheslav.chugunov.moniqo.network.mapper.CurrencyRatesMapper

internal class CurrencyNetworkRepositoryImpl(
    private val dataSource: CurrencyRemoteDataSource,
    private val mapper: CurrencyRatesMapper,
) : CurrencyNetworkRepository {
    override suspend fun getRates(): CurrencyRates = mapper.toDomain(baseCurrency = "eur", dto = dataSource.getEurRates())
}
