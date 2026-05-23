package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRed
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRangesBottomSheetComponent(
    initialGoodMax: Float,
    initialAverageMax: Float,
    onApply: (Float, Float) -> Unit,
    onDismiss: () -> Unit,
) {
    var goodMax by remember { mutableFloatStateOf(initialGoodMax) }
    var mediumMax by remember { mutableFloatStateOf(initialAverageMax) }

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
                text = stringResource(MR.strings.settings_edit_deal_ranges_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            RangeSlider(
                value = goodMax..mediumMax,
                onValueChange = { range ->
                    goodMax = range.start
                    mediumMax = range.endInclusive
                },
                valueRange = 1f..40f,
                modifier = Modifier.fillMaxWidth(),
                colors =
                    SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant,
                    ),
            )

            DealRangeRowComponent(
                label = stringResource(MR.strings.settings_good_deal),
                value =
                    remember(goodMax) {
                        "0–${goodMax.toInt()}%"
                    },
                color = MaterialTheme.colorScheme.goodGreen,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DealRangeRowComponent(
                label = stringResource(MR.strings.settings_medium_deal),
                value =
                    remember(goodMax, mediumMax) {
                        "${goodMax.toInt()}–${mediumMax.toInt()}%"
                    },
                color = MaterialTheme.colorScheme.mediumAmber,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DealRangeRowComponent(
                label = stringResource(MR.strings.settings_bad_deal),
                value =
                    remember(mediumMax) {
                        "${mediumMax.toInt()}%+"
                    },
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
                    Text(stringResource(MR.strings.settings_cancel))
                }
                Button(
                    onClick = {
                        onApply(goodMax, mediumMax)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(MR.strings.settings_apply))
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun EditRangesBottomSheetPreview() {
    MoniqoTheme {
        EditRangesBottomSheetComponent(initialGoodMax = 5f, initialAverageMax = 15f, onApply = { _, _ -> }, onDismiss = {})
    }
}
