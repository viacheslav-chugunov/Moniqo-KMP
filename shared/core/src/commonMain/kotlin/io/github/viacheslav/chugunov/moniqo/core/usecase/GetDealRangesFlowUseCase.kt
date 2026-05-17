package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns the deal-quality thresholds as a continuous stream.
 *
 * Emits [DealRanges] with defaults (good=5%, medium=10%) until custom values are saved
 * via [SetDealRangesUseCase] or reset via [ResetDealRangesUseCase].
 */
class GetDealRangesFlowUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    operator fun invoke(): Flow<DealRanges> = settingRepository.getDealRanges()
}
