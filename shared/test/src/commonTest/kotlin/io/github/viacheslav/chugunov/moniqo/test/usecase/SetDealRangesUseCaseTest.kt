package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetDealRangesUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SetDealRangesUseCaseTest {
    @Test
    fun `delegates to repository with correct ranges`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val ranges = DealRanges(3, 8)
            SetDealRangesUseCase(repo)(ranges)

            assertEquals(ranges, repo.savedDealRanges)
        }
}
