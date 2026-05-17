package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppThemeUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SetAppThemeUseCaseTest {
    @Test
    fun `delegates to repository with correct theme`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            SetAppThemeUseCase(repo)(AppTheme.DARK)

            assertEquals(AppTheme.DARK, repo.savedTheme)
        }
}
