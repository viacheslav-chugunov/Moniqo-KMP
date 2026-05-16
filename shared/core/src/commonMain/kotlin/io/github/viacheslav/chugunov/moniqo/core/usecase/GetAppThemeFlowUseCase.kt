package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

class GetAppThemeFlowUseCase(
    private val settingRepository: SettingStorageRepository
) {
    operator fun invoke(): Flow<AppTheme> =
        settingRepository.getAppTheme()
}
