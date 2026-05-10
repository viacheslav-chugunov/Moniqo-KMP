package io.github.viacheslav.chugunov.moniqo.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MoniqoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) MoniqoDarkColorScheme else MoniqoLightColorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content,
    )
}
