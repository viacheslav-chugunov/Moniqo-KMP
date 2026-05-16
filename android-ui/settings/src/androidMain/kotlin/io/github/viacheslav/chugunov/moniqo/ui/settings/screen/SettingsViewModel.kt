package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppLanguageFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppThemeFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetDealRangesFlowUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.ResetDealRangesUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppLanguageUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetAppThemeUseCase
import io.github.viacheslav.chugunov.moniqo.core.usecase.SetDealRangesUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    getAppThemeFlowUseCase: GetAppThemeFlowUseCase,
    getAppLanguageFlowUseCase: GetAppLanguageFlowUseCase,
    getDealRangesFlowUseCase: GetDealRangesFlowUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val setDealRangesUseCase: SetDealRangesUseCase,
    private val resetDealRangesUseCase: ResetDealRangesUseCase,
    private val settingsMapper: SettingsMapper,
) : AppViewModel<SettingsState, SettingsIntent, SettingsEffect>(SettingsState.Loading) {
    init {
        combineTransform<AppTheme, AppLanguage, DealRanges, SettingsState>(
            getAppThemeFlowUseCase(),
            getAppLanguageFlowUseCase(),
            getDealRangesFlowUseCase(),
        ) { theme, language, dealRanges ->
            emit(
                settingsMapper.toSettingsState(
                    theme = theme,
                    language = language,
                    dealRanges = dealRanges,
                    currentContentState = childState(),
                ),
            )
        }.onEach { state ->
            updateState { state }
        }.launchIn(viewModelScope)
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ChangeTheme -> changeTheme(intent.theme)
            is SettingsIntent.ChangeDealRanges -> changeDealRanges(intent.goodMax, intent.averageMax)
            SettingsIntent.ResetRanges -> resetRanges()
            SettingsIntent.OpenRangeEditor -> updateChildState<SettingsState.Content> { it.copy(isEditingRanges = true) }
            SettingsIntent.CloseRangeEditor -> updateChildState<SettingsState.Content> { it.copy(isEditingRanges = false) }
            is SettingsIntent.ChangeLanguage -> changeLanguage(intent.language)
            SettingsIntent.OpenLanguagePicker -> updateChildState<SettingsState.Content> { it.copy(isPickingLanguage = true) }
            SettingsIntent.CloseLanguagePicker -> updateChildState<SettingsState.Content> { it.copy(isPickingLanguage = false) }
        }
    }

    private fun changeTheme(theme: AppTheme) {
        viewModelScope.launch {
            setAppThemeUseCase(theme)
        }
    }

    private fun changeDealRanges(
        goodMax: Float,
        mediumMax: Float,
    ) {
        viewModelScope.launch {
            setDealRangesUseCase(
                DealRanges(
                    good = goodMax.toInt(),
                    medium = mediumMax.toInt(),
                ),
            )
        }
    }

    private fun resetRanges() {
        viewModelScope.launch {
            resetDealRangesUseCase()
        }
    }

    private fun changeLanguage(language: AppLanguage) {
        viewModelScope.launch {
            setAppLanguageUseCase(language)
        }
    }
}
