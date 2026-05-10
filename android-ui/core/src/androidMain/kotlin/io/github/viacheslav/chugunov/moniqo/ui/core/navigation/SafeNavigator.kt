package io.github.viacheslav.chugunov.moniqo.ui.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class SafeNavigator(private val backStack: NavBackStack<NavKey>) {
    fun navigate(route: NavKey) {
        backStack.add(route)
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}
