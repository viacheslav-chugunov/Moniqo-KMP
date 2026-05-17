package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns the persisted app theme as a continuous stream.
 *
 * Emits [AppTheme.SYSTEM] until a different theme has been explicitly saved
 * via [SetAppThemeUseCase].
 */
class GetAppThemeFlowUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    operator fun invoke(): Flow<AppTheme> = settingRepository.getAppTheme()
}
