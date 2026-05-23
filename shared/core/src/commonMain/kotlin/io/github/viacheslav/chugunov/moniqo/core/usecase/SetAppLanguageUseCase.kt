package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

/**
 * Persists a new app language selection.
 */
class SetAppLanguageUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke(language: AppLanguage) =
        settingRepository.setAppLanguage(language)
}
