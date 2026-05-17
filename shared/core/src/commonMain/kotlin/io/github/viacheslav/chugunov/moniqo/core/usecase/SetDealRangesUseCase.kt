package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

/**
 * Persists custom deal-quality thresholds.
 *
 * Both [DealRanges.good] and [DealRanges.medium] are stored independently;
 * call [ResetDealRangesUseCase] to restore defaults.
 */
class SetDealRangesUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke(ranges: DealRanges) = settingRepository.setDealRanges(ranges)
}
