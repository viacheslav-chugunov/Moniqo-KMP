package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns the most recently used currency codes as a continuous stream.
 *
 * Defaults to ["EUR", "USD"] and is capped at five entries. The list is kept in
 * most-recently-used order by [AddRecentCurrencyUseCase].
 */
class GetRecentCurrenciesFlowUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    operator fun invoke(): Flow<List<String>> = settingRepository.getRecentCurrencies()
}
