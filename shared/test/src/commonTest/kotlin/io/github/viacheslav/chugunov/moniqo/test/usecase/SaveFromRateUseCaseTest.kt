package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveFromRateUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairMock
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.RatePairStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveFromRateUseCaseTest {
    private val newFromRate = Rate("jpy", 150.0)

    @Test
    fun `replaces fromRate with provided rate`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            SaveFromRateUseCase(repo)(newFromRate)

            assertEquals(newFromRate, repo.savedPair?.fromRate)
        }

    @Test
    fun `preserves toRate`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            SaveFromRateUseCase(repo)(newFromRate)

            assertEquals(ratePairMock.toRate, repo.savedPair?.toRate)
        }

    @Test
    fun `preserves baseCurrency and updatedAt`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            SaveFromRateUseCase(repo)(newFromRate)

            assertEquals(ratePairMock.baseCurrency, repo.savedPair?.baseCurrency)
            assertEquals(ratePairMock.updatedAt, repo.savedPair?.updatedAt)
        }
}
