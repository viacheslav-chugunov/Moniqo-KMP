package io.github.viacheslav.chugunov.moniqo.ui.core.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@Composable
fun CurrencyPairSectionComponent(
    fromCurrency: CurrencyInfo,
    toCurrency: CurrencyInfo,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onSwapClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CurrencySelectorComponent(
                currency = fromCurrency,
                onClick = onFromClick,
                modifier = Modifier.weight(1f),
            )
            SwapButtonComponent(onClick = onSwapClick)
            CurrencySelectorComponent(
                currency = toCurrency,
                onClick = onToClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@ComponentPreview
@Composable
private fun CurrencyPairSectionComponentPreview() {
    MoniqoTheme {
        CurrencyPairSectionComponent(
            fromCurrency = CurrencyInfo("EUR", "Euro", "🇪🇺", isCrypto = false),
            toCurrency = CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false),
            onFromClick = {},
            onToClick = {},
            onSwapClick = {},
        )
    }
}
