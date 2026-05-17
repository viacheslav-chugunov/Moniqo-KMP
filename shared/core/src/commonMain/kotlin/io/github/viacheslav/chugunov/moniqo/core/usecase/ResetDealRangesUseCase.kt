package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

/**
 * Restores deal-quality thresholds to their defaults (good=5%, medium=10%).
 */
class ResetDealRangesUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke() = settingRepository.resetDealRanges()
}
