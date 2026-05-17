package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetDealRangesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetDealRangesFlowUseCaseTest {
    @Test
    fun `returns flow from repository`() =
        runTest {
            val ranges = DealRanges(3, 8)
            val repo = SettingStorageRepositoryMock(dealRanges = ranges)

            assertEquals(ranges, GetDealRangesFlowUseCase(repo)().first())
        }

    @Test
    fun `emits updated value when flow changes`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val useCase = GetDealRangesFlowUseCase(repo)
            val updated = DealRanges(2, 7)

            repo.dealRangesFlow.value = updated
            assertEquals(updated, useCase().first())
        }
}
