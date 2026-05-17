package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

/**
 * Persists a new app theme selection.
 */
class SetAppThemeUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke(theme: AppTheme) = settingRepository.setAppTheme(theme)
}
