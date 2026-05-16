package io.github.viacheslav.chugunov.moniqo.screen

import androidx.lifecycle.viewModelScope
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppThemeFlowUseCase
import io.github.viacheslav.chugunov.moniqo.ui.core.AppViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AppViewModel(
    getAppThemeFlowUseCase: GetAppThemeFlowUseCase,
    mapper: AppMapper
) : AppViewModel<AppState, AppIntent, AppEffect>(AppState.Loading) {

    init {
        getAppThemeFlowUseCase().onEach { theme ->
            updateState { mapper.toAppState(theme) }
        }.launchIn(viewModelScope)
    }

    override fun onIntent(intent: AppIntent) {
        // No-op
    }
}
