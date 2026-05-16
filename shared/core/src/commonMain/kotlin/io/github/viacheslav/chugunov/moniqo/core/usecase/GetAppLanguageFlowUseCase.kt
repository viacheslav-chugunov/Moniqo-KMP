package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

class GetAppLanguageFlowUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    operator fun invoke(): Flow<AppLanguage> = settingRepository.getAppLanguage()
}
