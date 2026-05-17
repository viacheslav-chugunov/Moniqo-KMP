package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns the persisted app language as a continuous stream.
 *
 * Emits [AppLanguage.SYSTEM] until a different language has been explicitly saved
 * via [SetAppLanguageUseCase].
 */
class GetAppLanguageFlowUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    operator fun invoke(): Flow<AppLanguage> = settingRepository.getAppLanguage()
}
