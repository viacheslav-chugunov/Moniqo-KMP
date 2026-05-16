package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import io.github.viacheslav.chugunov.moniqo.ui.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.ThemeMode

internal data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val goodDealMaxPercent: Float = DealRanges().goodMax,
    val averageDealMaxPercent: Float = DealRanges().averageMax,
    val isEditingRanges: Boolean = false,
)
