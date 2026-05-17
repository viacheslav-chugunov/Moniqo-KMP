package io.github.viacheslav.chugunov.moniqo.test.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetBaseRateCurrencyFlowUseCase
import io.github.viacheslav.chugunov.moniqo.test.mock.repository.SettingStorageRepositoryMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetBaseRateCurrencyFlowUseCaseTest {
    @Test
    fun `returns flow from repository`() =
        runTest {
            val usd = Currency.of("usd")
            val repo = SettingStorageRepositoryMock(baseRatesCurrency = usd)

            assertEquals(usd, GetBaseRateCurrencyFlowUseCase(repo)().first())
        }

    @Test
    fun `emits updated value when flow changes`() =
        runTest {
            val repo = SettingStorageRepositoryMock()
            val useCase = GetBaseRateCurrencyFlowUseCase(repo)
            val gbp = Currency.of("gbp")

            repo.baseRatesCurrencyFlow.value = gbp
            assertEquals(gbp, useCase().first())
        }
}
