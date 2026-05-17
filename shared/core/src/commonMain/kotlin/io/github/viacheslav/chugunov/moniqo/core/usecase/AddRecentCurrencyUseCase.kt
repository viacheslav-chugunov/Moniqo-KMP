package io.github.viacheslav.chugunov.moniqo.core.usecase

import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository

/**
 * Prepends [code] to the recent-currencies list, deduplicating and capping at five entries.
 *
 * If [code] already appears in the list it is moved to the front rather than duplicated.
 * The oldest entry beyond the five-item cap is dropped.
 */
class AddRecentCurrencyUseCase(
    private val settingRepository: SettingStorageRepository,
) {
    suspend operator fun invoke(code: String) = settingRepository.addRecentCurrency(code)
}
