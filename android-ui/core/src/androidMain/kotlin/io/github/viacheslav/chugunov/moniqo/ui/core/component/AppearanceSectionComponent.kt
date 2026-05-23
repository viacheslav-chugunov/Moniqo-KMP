package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.extensions.toLabel
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSectionComponent(
    themeMode: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    SettingsSectionComponent(title = stringResource(MR.strings.settings_appearance)) {
        val themes = AppTheme.entries
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            themes.forEachIndexed { index, theme ->
                SegmentedButton(
                    selected = themeMode == theme,
                    onClick = { onThemeChange(theme) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = themes.size),
                    label = { Text(theme.toLabel()) },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun AppearanceSectionPreview() {
    MoniqoTheme {
        AppearanceSectionComponent(themeMode = AppTheme.SYSTEM, onThemeChange = {})
    }
}
