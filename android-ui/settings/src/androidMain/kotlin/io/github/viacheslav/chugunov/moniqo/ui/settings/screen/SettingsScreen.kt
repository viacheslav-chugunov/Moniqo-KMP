package io.github.viacheslav.chugunov.moniqo.ui.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.viacheslav.chugunov.moniqo.android.ui.core.R
import io.github.viacheslav.chugunov.moniqo.ui.core.ScreenPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.ThemeMode
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRed
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmber
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreenContent(
        state = state,
        onBack = onBack,
        onThemeChange = { viewModel.onIntent(SettingsIntent.ChangeTheme(it)) },
        onApplyRanges = { good, average ->
            viewModel.onIntent(SettingsIntent.ChangeDealRanges(good, average))
        },
        onResetRanges = { viewModel.onIntent(SettingsIntent.ResetRanges) },
        onOpenRangeEditor = { viewModel.onIntent(SettingsIntent.OpenRangeEditor) },
        onCloseRangeEditor = { viewModel.onIntent(SettingsIntent.CloseRangeEditor) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    state: SettingsState,
    onBack: () -> Unit,
    onThemeChange: (ThemeMode) -> Unit,
    onApplyRanges: (Float, Float) -> Unit,
    onResetRanges: () -> Unit,
    onOpenRangeEditor: () -> Unit,
    onCloseRangeEditor: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AppearanceSection(themeMode = state.themeMode, onThemeChange = onThemeChange)
            LanguageSection()
            DealRangesSection(
                goodMax = state.goodDealMaxPercent,
                averageMax = state.averageDealMaxPercent,
                onEdit = onOpenRangeEditor,
                onReset = onResetRanges,
            )
            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }

    if (state.isEditingRanges) {
        EditRangesBottomSheet(
            initialGoodMax = state.goodDealMaxPercent,
            initialAverageMax = state.averageDealMaxPercent,
            onApply = onApplyRanges,
            onDismiss = onCloseRangeEditor,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceSection(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    SettingsSection(title = stringResource(R.string.settings_appearance)) {
        val modes = ThemeMode.entries
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            modes.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = themeMode == mode,
                    onClick = { onThemeChange(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = modes.size),
                    label = { Text(mode.toLabel()) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_language).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Card(
            onClick = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.settings_language),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.settings_language_english),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun DealRangesSection(
    goodMax: Float,
    averageMax: Float,
    onEdit: () -> Unit,
    onReset: () -> Unit,
) {
    SettingsSection(
        title = stringResource(R.string.settings_deal_ranges),
        trailingAction = {
            TextButton(onClick = onEdit) {
                Text(
                    text = stringResource(R.string.settings_edit_ranges),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
    ) {
        DealRangeRow(
            label = stringResource(R.string.settings_good_deal),
            value = "0–${goodMax.toInt()}%",
            color = MaterialTheme.colorScheme.goodGreen,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        DealRangeRow(
            label = stringResource(R.string.settings_medium_deal),
            value = "${goodMax.toInt()}–${averageMax.toInt()}%",
            color = MaterialTheme.colorScheme.mediumAmber,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        DealRangeRow(
            label = stringResource(R.string.settings_bad_deal),
            value = "${averageMax.toInt()}%+",
            color = MaterialTheme.colorScheme.badRed,
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        TextButton(
            onClick = onReset,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = stringResource(R.string.settings_reset_ranges),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditRangesBottomSheet(
    initialGoodMax: Float,
    initialAverageMax: Float,
    onApply: (Float, Float) -> Unit,
    onDismiss: () -> Unit,
) {
    var goodMax by remember { mutableFloatStateOf(initialGoodMax) }
    var averageMax by remember { mutableFloatStateOf(initialAverageMax) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_edit_deal_ranges_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            RangeSlider(
                value = goodMax..averageMax,
                onValueChange = { range ->
                    goodMax = range.start
                    averageMax = range.endInclusive
                },
                valueRange = 0f..40f,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    ),
            )

            DealRangeRow(
                label = stringResource(R.string.settings_good_deal),
                value = "0–${goodMax.toInt()}%",
                color = MaterialTheme.colorScheme.goodGreen,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DealRangeRow(
                label = stringResource(R.string.settings_medium_deal),
                value = "${goodMax.toInt()}–${averageMax.toInt()}%",
                color = MaterialTheme.colorScheme.mediumAmber,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DealRangeRow(
                label = stringResource(R.string.settings_bad_deal),
                value = "${averageMax.toInt()}%+",
                color = MaterialTheme.colorScheme.badRed,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.settings_cancel))
                }
                Button(
                    onClick = { onApply(goodMax, averageMax) },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.settings_apply))
                }
            }
        }
    }
}

@Composable
private fun DealRangeRow(
    label: String,
    value: String,
    color: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = color,
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    trailingAction: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = if (trailingAction != null) 8.dp else 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            trailingAction?.invoke()
        }
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content,
            )
        }
    }
}

@Composable
private fun ThemeMode.toLabel(): String =
    when (this) {
        ThemeMode.LIGHT -> stringResource(R.string.settings_theme_light)
        ThemeMode.DARK -> stringResource(R.string.settings_theme_dark)
        ThemeMode.SYSTEM -> stringResource(R.string.settings_theme_system)
    }

@ScreenPreview
@Composable
private fun SettingsScreenContentPreview() {
    MoniqoTheme {
        SettingsScreenContent(
            state = SettingsState(),
            onBack = {},
            onThemeChange = {},
            onApplyRanges = { _, _ -> },
            onResetRanges = {},
            onOpenRangeEditor = {},
            onCloseRangeEditor = {},
        )
    }
}
