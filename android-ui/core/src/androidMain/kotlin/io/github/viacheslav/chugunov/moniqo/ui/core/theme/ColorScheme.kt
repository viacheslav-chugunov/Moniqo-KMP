package io.github.viacheslav.chugunov.moniqo.ui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val MoniqoLightColorScheme =
    lightColorScheme(
        primary = Color(0xFF2563EB),
        primaryContainer = Color(0xFFDBEAFE),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFEFF1F7),
        background = Color(0xFFF7F8FA),
        onPrimary = Color(0xFFFFFFFF),
        onSurface = Color(0xFF111827),
        onSurfaceVariant = Color(0xFF6B7280),
        outline = Color(0xFFE5E7EB),
        outlineVariant = Color(0xFFF3F4F6),
    )

internal val MoniqoDarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF60A5FA),
        primaryContainer = Color(0xFF1E3A6E),
        surface = Color(0xFF1F2937),
        surfaceVariant = Color(0xFF374151),
        background = Color(0xFF111827),
        onPrimary = Color(0xFF0F172A),
        onSurface = Color(0xFFF9FAFB),
        onSurfaceVariant = Color(0xFF9CA3AF),
        outline = Color(0xFF374151),
        outlineVariant = Color(0xFF1F2937),
    )

val ColorScheme.goodGreen
    get() = if (this === MoniqoDarkColorScheme) Color(0xFF34D399) else Color(0xFF059669)

val ColorScheme.goodGreenContainer
    get() = if (this === MoniqoDarkColorScheme) Color(0xFF064E3B) else Color(0xFFD1FAE5)

val ColorScheme.mediumAmber
    get() = if (this === MoniqoDarkColorScheme) Color(0xFFFBBF24) else Color(0xFFD97706)

val ColorScheme.mediumAmberContainer
    get() = if (this === MoniqoDarkColorScheme) Color(0xFF78350F) else Color(0xFFFEF3C7)

val ColorScheme.badRed
    get() = if (this === MoniqoDarkColorScheme) Color(0xFFF87171) else Color(0xFFDC2626)

val ColorScheme.badRedContainer
    get() = if (this === MoniqoDarkColorScheme) Color(0xFF7F1D1D) else Color(0xFFFEE2E2)
