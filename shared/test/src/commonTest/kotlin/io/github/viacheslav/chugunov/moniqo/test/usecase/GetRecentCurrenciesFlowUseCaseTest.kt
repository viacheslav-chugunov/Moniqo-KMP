package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.usecase.GetRecentCurrenciesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetRecentCurrenciesFlowUseCaseTest {
    @Test
    fun `returns flow from repository`() =
        runTest {
            val currencies = listOf("GBP", "JPY", "EUR")
            val repo = SettingStorageRepositoryMock(recentCurrencies = currencies)

            assertEquals(currencies, GetRecentCurrenciesFlowUseCase(repo)().first())
        }

    @Test
    fun `emits updated value when flow changes`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val useCase = GetRecentCurrenciesFlowUseCase(repo)
            val updated = listOf("CHF")

            repo.recentCurrenciesFlow.value = updated
            assertEquals(updated, useCase().first())
        }
}
