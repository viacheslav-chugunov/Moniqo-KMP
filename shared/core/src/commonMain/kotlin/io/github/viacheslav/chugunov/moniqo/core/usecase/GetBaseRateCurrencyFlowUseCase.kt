package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

class GetBaseRateCurrencyFlowUseCase(
    private val settingStorageRepository: SettingStorageRepository,
) {
    operator fun invoke() = settingStorageRepository.getBaseRatesCurrency()
}
