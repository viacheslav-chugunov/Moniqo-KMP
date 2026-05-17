package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

/**
 * Returns the base currency used on the Rates screen as a continuous stream.
 *
 * Defaults to EUR until a different currency has been explicitly saved via
 * [SetBaseRatesCurrencyUseCase].
 */
class GetBaseRateCurrencyFlowUseCase(
    private val settingStorageRepository: SettingStorageRepository,
) {
    operator fun invoke() = settingStorageRepository.getBaseRatesCurrency()
}
