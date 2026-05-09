package io.github.viacheslav.chugunov.moniqo.test.mock.datasource

import io.github.viacheslav.chugunov.moniqo.core.model.RatePair
import io.github.viacheslav.chugunov.moniqo.storage.datasource.RatePairFallbackDataSource
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairFallbackMock

class RatePairFallbackDataSourceMock(
    private val pair: RatePair = ratePairFallbackMock,
) : RatePairFallbackDataSource {
    override suspend fun get() = pair
}
