package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import io.github.viacheslav.chugunov.moniqo.ui.core.theme.ThemeMode

internal sealed interface SettingsIntent {
    data class ChangeTheme(val mode: ThemeMode) : SettingsIntent
    data class ChangeDealRanges(val goodMax: Float, val averageMax: Float) : SettingsIntent
    data object ResetRanges : SettingsIntent
    data object OpenRangeEditor : SettingsIntent
    data object CloseRangeEditor : SettingsIntent
}
