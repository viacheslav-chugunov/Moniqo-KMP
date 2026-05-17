package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.usecase.GetSplashReadyFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SplashRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetSplashReadyFlowUseCaseTest {
    @Test
    fun `returns false initially`() =
        runTest {
            assertFalse(GetSplashReadyFlowUseCase(SplashRepositoryMock())().first())
        }

    @Test
    fun `returns true after setReady is called`() =
        runTest {
            val repo = SplashRepositoryMock()
            repo.setReady()

            assertTrue(GetSplashReadyFlowUseCase(repo)().first())
        }
}
