package io.github.viacheslav.chugunov.moniqo.test.repository

import io.github.viacheslav.chugunov.moniqo.storage.repository.RatePairStorageRepositoryImpl
import io.github.viacheslav.chugunov.moniqo.test.mock.datasource.RatePairFallbackDataSourceMock
import io.github.viacheslav.chugunov.moniqo.test.mock.datasource.RatePairLocalDataSourceMock
import io.github.viacheslav.chugunov.moniqo.test.mock.mapper.RatePairStorageMapperMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairEntityMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairFallbackMock
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RatePairStorageRepositoryImplTest {
    @Test
    fun `saveRatePair delegates to localDataSource`() =
        runTest {
            val localDataSource = RatePairLocalDataSourceMock()
            makeRepo(localDataSource = localDataSource).saveRatePair(ratePairMock)

            assertEquals(ratePairMock, localDataSource.savedPair)
        }

    @Test
    fun `getRatePairAsFlow emits mapped entity when local has value`() =
        runTest {
            assertEquals(ratePairMock, makeRepo().getRatePairAsFlow().first())
        }

    @Test
    fun `getRatePairAsFlow emits fallback when local emits null`() =
        runTest {
            val result =
                makeRepo(
                    localDataSource = RatePairLocalDataSourceMock(initialEntity = null),
                ).getRatePairAsFlow().first()

            assertEquals(ratePairFallbackMock, result)
        }

    @Test
    fun `getRatePairAsFlow passes correct entity to mapper`() =
        runTest {
            val mapper = RatePairStorageMapperMock()
            makeRepo(mapper = mapper).getRatePairAsFlow().first()

            assertEquals(ratePairEntityMock, mapper.lastEntity)
        }

    @Test
    fun `getRatePairAsFlow switches to mapped entity when flow updates`() =
        runTest {
            val localDataSource = RatePairLocalDataSourceMock(initialEntity = null)
            val repo = makeRepo(localDataSource = localDataSource)

            assertEquals(ratePairFallbackMock, repo.getRatePairAsFlow().first())

            localDataSource.flow.value = ratePairEntityMock
            assertEquals(ratePairMock, repo.getRatePairAsFlow().first())
        }

    private fun makeRepo(
        localDataSource: RatePairLocalDataSourceMock = RatePairLocalDataSourceMock(),
        mapper: RatePairStorageMapperMock = RatePairStorageMapperMock(),
    ) = RatePairStorageRepositoryImpl(
        localDataSource = localDataSource,
        fallbackDataSource = RatePairFallbackDataSourceMock(),
        mapper = mapper,
    )
}
