package io.github.viacheslav.chugunov.moniqo.test.mock.datasource

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.db.RatePairEntity
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairEntityMock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RatePairLocalDataSourceMock(
    initialEntity: RatePairEntity? = ratePairEntityMock,
) : RatePairLocalDataSource {
    val flow = MutableStateFlow(initialEntity)
    var savedPair: RatePair? = null

    override suspend fun save(ratePair: RatePair) {
        savedPair = ratePair
    }

    override fun getAsFlow(): Flow<RatePairEntity?> = flow
}
