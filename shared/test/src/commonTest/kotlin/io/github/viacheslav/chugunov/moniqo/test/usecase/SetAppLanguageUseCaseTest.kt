package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppLanguageUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SetAppLanguageUseCaseTest {
    @Test
    fun `delegates to repository with correct language`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            SetAppLanguageUseCase(repo)(AppLanguage.RUSSIAN)

            assertEquals(AppLanguage.RUSSIAN, repo.savedLanguage)
        }
}
