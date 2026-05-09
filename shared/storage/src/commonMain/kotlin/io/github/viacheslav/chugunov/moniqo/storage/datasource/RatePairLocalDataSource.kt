package io.github.viacheslav.chugunov.moniqo.storage.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.storage.db.AppDatabase
import io.github.viacheslav.chugunov.moniqo.storage.db.RatePairEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface RatePairLocalDataSource {
    suspend fun save(ratePair: RatePair)

    fun getAsFlow(): Flow<RatePairEntity?>
}

internal class RatePairLocalDataSourceImpl(
    database: AppDatabase,
    private val dispatchers: CoroutineDispatchers,
) : RatePairLocalDataSource {
    private val ratePairEntityQueries = database.ratePairEntityQueries

    override suspend fun save(ratePair: RatePair) =
        withContext(dispatchers.io) {
            ratePairEntityQueries.transaction {
                ratePairEntityQueries.clearRatePair()
                ratePairEntityQueries.insertRatePair(
                    from_currency = ratePair.fromRate.currency,
                    from_rate = ratePair.fromRate.rate,
                    to_currency = ratePair.toRate.currency,
                    to_rate = ratePair.toRate.rate,
                    base_currency = ratePair.baseCurrency,
                    updated_at = ratePair.updatedAt,
                )
            }
        }

    override fun getAsFlow(): Flow<RatePairEntity?> = ratePairEntityQueries.getRatePair().asFlow().mapToOneOrNull(dispatchers.io)
}
