package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.Rate
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRatePairFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.model.ratePairMock
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.RatePairStorageRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetRatePairFlowUseCaseTest {
    @Test
    fun `returns flow from repository`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()

            assertEquals(ratePairMock, GetRatePairFlowUseCase(repo)().first())
        }

    @Test
    fun `emits updated value when flow changes`() =
        runTest {
            val repo = RatePairStorageRepositoryMock()
            val updated = ratePairMock.copy(fromRate = Rate(Currency.of("jpy"), 150.0))

            val useCase = GetRatePairFlowUseCase(repo)
            assertEquals(ratePairMock, useCase().first())
            repo.flow.value = updated

            assertEquals(updated, useCase().first())
        }
}
