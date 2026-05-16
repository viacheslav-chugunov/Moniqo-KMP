package io.github.viacheslav.chugunov.moniqo.storage.repository

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.repository.CurrencyStorageRepository
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.mapper.CurrencyStorageMapper

internal class CurrencyStorageRepositoryImpl(
    private val localDataSource: CurrencyLocalDataSource,
    private val fallbackDataSource: CurrencyFallbackDataSource,
    private val mapper: CurrencyStorageMapper,
) : CurrencyStorageRepository {
    override suspend fun saveRates(rates: CurrencyRates) = localDataSource.save(rates)

    override suspend fun getRates(): CurrencyRates {
        val record = localDataSource.get() ?: return fallbackDataSource.get()
        return mapper.toDomain(record.entity, record.rates)
    }

    override suspend fun isEmpty(): Boolean = localDataSource.get() == null
}
