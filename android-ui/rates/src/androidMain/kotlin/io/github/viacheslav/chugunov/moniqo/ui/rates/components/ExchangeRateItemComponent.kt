package io.github.viacheslav.chugunov.moniqo.ui.rates.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.viacheslav.chugunov.moniqo.ui.core.ComponentPreview
import io.github.viacheslav.chugunov.moniqo.ui.core.CurrencyListItemComponent
import io.github.viacheslav.chugunov.moniqo.ui.core.model.CurrencyInfo
import io.github.viacheslav.chugunov.moniqo.ui.core.theme.MoniqoTheme
import io.github.viacheslav.chugunov.moniqo.ui.rates.model.RateItem

@Composable
internal fun ExchangeRateItemComponent(
    rate: RateItem,
    modifier: Modifier = Modifier,
) {
    CurrencyListItemComponent(
        currency = rate.currency,
        trailingText = rate.rate,
        modifier = modifier,
    )
}

@ComponentPreview
@Composable
private fun ExchangeRateItemComponentPreview() {
    MoniqoTheme {
        ExchangeRateItemComponent(
            rate = RateItem(CurrencyInfo("USD", "US Dollar", "🇺🇸", isCrypto = false), "1.0934"),
        )
    }
}
