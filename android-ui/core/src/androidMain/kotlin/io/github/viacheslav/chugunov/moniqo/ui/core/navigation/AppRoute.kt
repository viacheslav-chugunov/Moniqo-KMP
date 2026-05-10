package io.github.viacheslav.chugunov.moniqo.ui.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppRoute : NavKey {
    @Serializable
    data object Home : AppRoute

    @Serializable
    data object Rates : AppRoute

    @Serializable
    data object Settings : AppRoute
}
