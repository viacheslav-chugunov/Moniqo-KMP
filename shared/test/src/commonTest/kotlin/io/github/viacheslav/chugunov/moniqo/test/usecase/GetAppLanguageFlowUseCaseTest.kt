package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppLanguageFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAppLanguageFlowUseCaseTest {
    @Test
    fun `returns flow from repository`() =
        runTest {
            val repo = SettingStorageRepositoryMock(appLanguage = AppLanguage.ENGLISH)

            assertEquals(AppLanguage.ENGLISH, GetAppLanguageFlowUseCase(repo)().first())
        }

    @Test
    fun `emits updated value when flow changes`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val useCase = GetAppLanguageFlowUseCase(repo)

            repo.appLanguageFlow.value = AppLanguage.RUSSIAN
            assertEquals(AppLanguage.RUSSIAN, useCase().first())
        }
}
