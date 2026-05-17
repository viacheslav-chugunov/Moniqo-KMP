package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetBaseRatesCurrencyUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SetBaseRatesCurrencyUseCaseTest {
    @Test
    fun `delegates to repository with correct currency`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val usd = Currency.of("usd")
            SetBaseRatesCurrencyUseCase(repo)(usd)

            assertEquals(usd, repo.savedBaseCurrency)
        }
}
