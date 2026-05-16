package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

class SetDealRangesUseCase(
    private val settingRepository: SettingStorageRepository
) {
    suspend operator fun invoke(ranges: DealRanges) =
        settingRepository.setDealRanges(ranges)
}
