package io.github.viacheslav.chugunov.moniqo.core.repository

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import kotlinx.coroutines.flow.Flow

interface RatePairStorageRepository {
    suspend fun saveRatePair(ratePair: RatePair)

    fun getRatePairAsFlow(): Flow<RatePair>
}
