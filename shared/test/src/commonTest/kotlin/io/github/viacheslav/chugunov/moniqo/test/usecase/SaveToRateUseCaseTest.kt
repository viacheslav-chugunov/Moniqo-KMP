package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.usecase.SaveToRateUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairMock
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.RatePairStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveToRateUseCaseTest {
    private val newToRate = Rate("jpy", 150.0)

    @Test
    fun `replaces toRate with provided rate`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            SaveToRateUseCase(repo)(newToRate)

            assertEquals(newToRate, repo.savedPair?.toRate)
        }

    @Test
    fun `preserves fromRate`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            SaveToRateUseCase(repo)(newToRate)

            assertEquals(ratePairMock.fromRate, repo.savedPair?.fromRate)
        }

    @Test
    fun `preserves baseCurrency and updatedAt`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            SaveToRateUseCase(repo)(newToRate)

            assertEquals(ratePairMock.baseCurrency, repo.savedPair?.baseCurrency)
            assertEquals(ratePairMock.updatedAt, repo.savedPair?.updatedAt)
        }
}
