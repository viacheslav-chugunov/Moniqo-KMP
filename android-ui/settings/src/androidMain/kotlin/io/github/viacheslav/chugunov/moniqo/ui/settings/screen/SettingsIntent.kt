package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme

internal sealed interface SettingsIntent {
    data class ChangeTheme(val theme: AppTheme) : SettingsIntent

    data class ChangeDealRanges(val goodMax: Float, val averageMax: Float) : SettingsIntent

    data object ResetRanges : SettingsIntent

    data object OpenRangeEditor : SettingsIntent

    data object CloseRangeEditor : SettingsIntent

    data class ChangeLanguage(val language: AppLanguage) : SettingsIntent

    data object OpenLanguagePicker : SettingsIntent

    data object CloseLanguagePicker : SettingsIntent
}
