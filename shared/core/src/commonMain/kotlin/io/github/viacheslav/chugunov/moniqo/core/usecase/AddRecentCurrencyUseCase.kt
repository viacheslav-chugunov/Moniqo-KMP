package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

class AddRecentCurrencyUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke(code: String) = settingRepository.addRecentCurrency(code)
}
