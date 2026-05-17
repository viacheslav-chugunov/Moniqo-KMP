package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.usecase.AddRecentCurrencyUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AddRecentCurrencyUseCaseTest {
    @Test
    fun `delegates to repository with correct code`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            AddRecentCurrencyUseCase(repo)("GBP")

            assertEquals("GBP", repo.addedCurrencyCode)
        }
}
