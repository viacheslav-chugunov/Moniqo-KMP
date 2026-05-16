package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

class GetDealRangesFlowUseCase(
    private val settingRepository: SettingStorageRepository
) {
    operator fun invoke(): Flow<DealRanges> =
        settingRepository.getDealRanges()
}
