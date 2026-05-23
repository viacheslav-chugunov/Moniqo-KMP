package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.core.MR
import io.github.viacheslav.chugunov.moniqo.core.model.DealQuality
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.model.ExchangeAnalysis
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.badRed
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.goodGreen
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.mediumAmber

@Composable
fun ExchangeAnalysisCardComponent(
    analysis: ExchangeAnalysis,
    modifier: Modifier = Modifier,
) {
    val accentColor =
        when (analysis.quality) {
            DealQuality.GOOD -> MaterialTheme.colorScheme.goodGreen
            DealQuality.MEDIUM -> MaterialTheme.colorScheme.mediumAmber
            DealQuality.BAD -> MaterialTheme.colorScheme.badRed
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(MR.strings.exchange_analysis_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                DealBadgeComponent(quality = analysis.quality)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AnalysisColumnComponent(
                    label = stringResource(MR.strings.exchange_analysis_official_rate),
                    value = analysis.officialRate,
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                AnalysisColumnComponent(
                    label = stringResource(MR.strings.exchange_analysis_your_rate),
                    value = analysis.enteredRate,
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AnalysisColumnComponent(
                    label = stringResource(MR.strings.exchange_analysis_difference),
                    value = analysis.differencePercent,
                    valueColor = accentColor,
                    modifier = Modifier.weight(1f),
                )
                AnalysisColumnComponent(
                    label = analysis.lossOrProfitLabel,
                    value = analysis.lossOrProfitAmount,
                    valueColor = accentColor,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ExchangeAnalysisCardComponentPreview() {
    MoniqoTheme {
        ExchangeAnalysisCardComponent(
            analysis =
                ExchangeAnalysis(
                    officialRate = "1 EUR = 1.17 USD",
                    enteredRate = "1 EUR = 1.20 USD",
                    differencePercent = "2.56%",
                    lossOrProfitLabel = "Loss",
                    lossOrProfitAmount = "0.03 EUR\n0.04 USD",
                    quality = DealQuality.GOOD,
                ),
        )
    }
}
