package io.github.viacheslav.chugunov.moniqo.storage.repository

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.repository.RatePairStorageRepository
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.mapper.RatePairStorageMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RatePairStorageRepositoryImpl(
    private val localDataSource: RatePairLocalDataSource,
    private val fallbackDataSource: RatePairFallbackDataSource,
    private val mapper: RatePairStorageMapper,
) : RatePairStorageRepository {
    override suspend fun saveRatePair(ratePair: RatePair) = localDataSource.save(ratePair)

    override fun getRatePairAsFlow(): Flow<RatePair> =
        localDataSource.getAsFlow().map { entity ->
            entity?.let { mapper.toDomain(it) } ?: fallbackDataSource.get()
        }
}
