package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

class SetAppLanguageUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke(theme: AppLanguage) = settingRepository.setAppLanguage(theme)
}
