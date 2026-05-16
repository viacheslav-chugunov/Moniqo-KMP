package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

class SetBaseRatesCurrencyUseCase(
    private val settingStorageRepository: SettingStorageRepository
) {
    suspend operator fun invoke(currency: Currency) =
        settingStorageRepository.setBaseRatesCurrency(currency)
}
