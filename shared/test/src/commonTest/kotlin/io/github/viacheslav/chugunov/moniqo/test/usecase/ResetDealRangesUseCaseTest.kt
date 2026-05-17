package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.usecase.ResetDealRangesUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ResetDealRangesUseCaseTest {
    @Test
    fun `delegates to repository`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            ResetDealRangesUseCase(repo)()

            assertTrue(repo.resetDealRangesCalled)
        }
}
