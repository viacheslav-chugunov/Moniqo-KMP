package io.github.viacheslav.chugunov.moniqo.ui.core.extensions

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.compose.stringResource
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage

@Composable
internal fun AppLanguage.displayName(): String =
    when (this) {
        AppLanguage.SYSTEM -> stringResource(MR.strings.settings_language_system)
        AppLanguage.ENGLISH -> stringResource(MR.strings.settings_language_english_native)
        AppLanguage.RUSSIAN -> stringResource(MR.strings.settings_language_russian_native)
        AppLanguage.LATVIAN -> stringResource(MR.strings.settings_language_latvian_native)
    }
