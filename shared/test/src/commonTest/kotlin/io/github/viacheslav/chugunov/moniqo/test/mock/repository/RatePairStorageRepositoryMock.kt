package io.github.viacheslav.chugunov.moniqo.test.mock.repository

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.core.repository.RatePairStorageRepository
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairMock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RatePairStorageRepositoryMock(
    initialPair: RatePair = ratePairMock,
) : RatePairStorageRepository {
    val flow = MutableStateFlow(initialPair)
    var savedPair: RatePair? = null

    override suspend fun saveRatePair(ratePair: RatePair) {
        savedPair = ratePair
    }

    override fun getRatePairAsFlow(): Flow<RatePair> = flow
}
