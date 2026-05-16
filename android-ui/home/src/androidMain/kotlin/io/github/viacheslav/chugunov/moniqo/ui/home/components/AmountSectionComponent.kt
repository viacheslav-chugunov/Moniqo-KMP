package io.github.viacheslav.chugunov.moniqo.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme

@Composable
internal fun AmountSectionComponent(
    fromCurrency: CurrencyInfo,
    toCurrency: CurrencyInfo,
    fromAmount: String,
    toAmount: String,
    fromHint: String,
    toHint: String,
    onFromAmountChange: (String) -> Unit,
    onToAmountChange: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column {
            AmountInputFieldComponent(
                currency = fromCurrency,
                amount = fromAmount,
                hint = fromHint,
                isActive = true,
                onAmountChange = onFromAmountChange,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            AmountInputFieldComponent(
                currency = toCurrency,
                amount = toAmount,
                hint = toHint,
                isActive = false,
                onAmountChange = onToAmountChange,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun AmountSectionComponentPreview() {
    MoniqoTheme {
        AmountSectionComponent(
            fromCurrency = CurrencyInfo("EUR", "Euro", "🇪🇺", isCrypto = false),
            toCurrency = CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false),
            fromAmount = "1000",
            toAmount = "1200",
            fromHint = "Official: 1 EUR = 1.1700 USD",
            toHint = "At official rate ≈ 1,170.00 USD",
            onFromAmountChange = {},
            onToAmountChange = {},
        )
    }
}
