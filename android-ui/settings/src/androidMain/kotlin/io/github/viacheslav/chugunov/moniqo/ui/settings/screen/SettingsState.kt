package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme

internal sealed interface SettingsState {
    data object Loading : SettingsState
    data class Content(
        val theme: AppTheme,
        val goodDealMaxPercent: Float,
        val mediumDealMaxPercent: Float,
        val language: AppLanguage,
        val isEditingRanges: Boolean,
        val isPickingLanguage: Boolean,
    ) : SettingsState {

        companion object {
            val PREVIEW = Content(
                theme = AppTheme.SYSTEM,
                goodDealMaxPercent = 5f,
                mediumDealMaxPercent = 10f,
                language = AppLanguage.SYSTEM,
                isEditingRanges = false,
                isPickingLanguage = false
            )
        }
    }
}
