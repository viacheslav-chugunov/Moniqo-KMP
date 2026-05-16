package io.github.viacheslav.chugunov.moniqo.screen

import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme

interface AppMapper {
    fun toAppState(
        theme: AppTheme,
    ): AppState.Content
}

class AppMapperImpl : AppMapper {
    override fun toAppState(
        theme: AppTheme,
    ): AppState.Content = AppState.Content(
        theme = theme
    )

}
