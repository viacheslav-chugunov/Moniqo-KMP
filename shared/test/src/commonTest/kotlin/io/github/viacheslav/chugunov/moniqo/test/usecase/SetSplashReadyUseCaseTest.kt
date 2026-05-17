package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.usecase.SetSplashReadyUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SplashRepositoryMock
import kotlin.test.Test
import kotlin.test.assertTrue

class SetSplashReadyUseCaseTest {
    @Test
    fun `delegates setReady to repository`() {
        val repo = SplashRepositoryMock()
        SetSplashReadyUseCase(repo)()

        assertTrue(repo.setReadyCalled)
    }
}
