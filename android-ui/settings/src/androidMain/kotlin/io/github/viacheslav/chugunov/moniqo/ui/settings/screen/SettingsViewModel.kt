package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import io.github.viacheslav.chugunov.moniqo.ui.core.AppSettingsHolder
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import io.github.viacheslav.chugunov.moniqo.ui.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.ThemeMode

internal class SettingsViewModel(
    private val appSettingsHolder: AppSettingsHolder,
) : AppViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState =
        appSettingsHolder.settings.value.let { s ->
            SettingsState(
                themeMode = s.themeMode,
                goodDealMaxPercent = s.dealRanges.goodMax,
                averageDealMaxPercent = s.dealRanges.averageMax,
            )
        },
) {
    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ChangeTheme -> changeTheme(intent.mode)
            is SettingsIntent.ChangeDealRanges -> changeDealRanges(intent.goodMax, intent.averageMax)
            SettingsIntent.ResetRanges -> resetRanges()
            SettingsIntent.OpenRangeEditor -> updateState { it.copy(isEditingRanges = true) }
            SettingsIntent.CloseRangeEditor -> updateState { it.copy(isEditingRanges = false) }
        }
    }

    private fun changeTheme(mode: ThemeMode) {
        updateState { it.copy(themeMode = mode) }
        appSettingsHolder.updateTheme(mode)
    }

    private fun changeDealRanges(goodMax: Float, averageMax: Float) {
        val good = goodMax.coerceIn(0f, 40f)
        val average = averageMax.coerceIn(good, 40f)
        updateState { it.copy(goodDealMaxPercent = good, averageDealMaxPercent = average, isEditingRanges = false) }
        appSettingsHolder.updateDealRanges(DealRanges(good, average))
    }

    private fun resetRanges() {
        val defaults = DealRanges()
        updateState { it.copy(goodDealMaxPercent = defaults.goodMax, averageDealMaxPercent = defaults.averageMax) }
        appSettingsHolder.updateDealRanges(defaults)
    }
}
