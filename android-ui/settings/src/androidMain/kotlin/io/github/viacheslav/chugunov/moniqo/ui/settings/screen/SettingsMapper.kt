package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges

internal interface SettingsMapper {
    fun toSettingsState(
        theme: AppTheme,
        language: AppLanguage,
        dealRanges: DealRanges,
        currentContentState: SettingsState.Content?,
    ): SettingsState.Content
}

internal class SettingsMapperImpl : SettingsMapper {
    override fun toSettingsState(
        theme: AppTheme,
        language: AppLanguage,
        dealRanges: DealRanges,
        currentContentState: SettingsState.Content?,
    ): SettingsState.Content {
        return SettingsState.Content(
            theme = theme,
            language = language,
            goodDealMaxPercent = dealRanges.good.toFloat(),
            mediumDealMaxPercent = dealRanges.medium.toFloat(),
            isEditingRanges = currentContentState?.isEditingRanges ?: false,
            isPickingLanguage = currentContentState?.isPickingLanguage ?: false,
        )
    }
}
