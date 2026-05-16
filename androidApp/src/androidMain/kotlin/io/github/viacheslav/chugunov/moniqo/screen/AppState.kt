package io.github.viacheslav.chugunov.moniqo.screen

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme

sealed interface AppState {
    data object Loading : AppState
    data class Content(
        val theme: AppTheme,
    ) : AppState
}
