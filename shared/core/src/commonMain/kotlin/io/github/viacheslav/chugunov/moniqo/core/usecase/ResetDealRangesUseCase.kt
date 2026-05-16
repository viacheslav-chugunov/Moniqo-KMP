package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

class ResetDealRangesUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke() = settingRepository.resetDealRanges()
}
