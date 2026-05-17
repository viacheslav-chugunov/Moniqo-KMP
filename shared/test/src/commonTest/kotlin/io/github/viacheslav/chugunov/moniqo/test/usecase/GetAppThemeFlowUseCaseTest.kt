package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppThemeFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAppThemeFlowUseCaseTest {
    @Test
    fun `returns flow from repository`() =
        runTest {
            val repo = SettingStorageRepositoryMock(appTheme = AppTheme.DARK)

            assertEquals(AppTheme.DARK, GetAppThemeFlowUseCase(repo)().first())
        }

    @Test
    fun `emits updated value when flow changes`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val useCase = GetAppThemeFlowUseCase(repo)

            repo.appThemeFlow.value = AppTheme.LIGHT
            assertEquals(AppTheme.LIGHT, useCase().first())
        }
}
