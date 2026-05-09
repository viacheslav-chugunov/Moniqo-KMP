package io.github.viacheslav.chugunov.moniqo.storage.datasource

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.core.model.Rate

internal interface CurrencyFallbackDataSource {
    fun get(): CurrencyRates
}

internal class CurrencyFallbackDataSourceImpl : CurrencyFallbackDataSource {
    override fun get(): CurrencyRates = CurrencyRates(
        updatedAt = "2026-05-08",
        baseCurrency = "eur",
        rates = listOf(
            Rate("usd", 1.172758),
            Rate("gbp", 0.865056),
            Rate("jpy", 183.999967),
            Rate("chf", 0.915461),
            Rate("cad", 1.602029),
            Rate("aud", 1.626358),
            Rate("nzd", 1.975359),
            Rate("sek", 10.886249),
            Rate("nok", 10.924122),
            Rate("dkk", 7.472406),
            Rate("hkd", 9.182106),
            Rate("sgd", 1.488307),
            Rate("cny", 7.976927),
            Rate("krw", 1717.510633),
            Rate("inr", 110.776888),
            Rate("brl", 5.800696),
            Rate("mxn", 20.276408),
            Rate("rub", 87.546452),
            Rate("try", 53.157857),
            Rate("pln", 4.23126),
            Rate("czk", 24.308229),
            Rate("huf", 356.971281),
            Rate("ron", 5.263458),
            Rate("ils", 3.402589),
            Rate("sar", 4.433231),
            Rate("aed", 4.306957),
            Rate("thb", 37.819083),
            Rate("myr", 4.594288),
            Rate("idr", 20371.048861),
            Rate("php", 70.904733),
            Rate("uah", 51.517686),
            Rate("zar", 19.299323),
            Rate("btc", 0.000014735266),
            Rate("eth", 0.00051449711),
            Rate("bnb", 0.0018337054),
            Rate("sol", 0.013285104),
            Rate("xrp", 0.84677236),
            Rate("ada", 4.47527698),
            Rate("doge", 11.01385248),
            Rate("dot", 0.89699929),
            Rate("pol", 11.87815688),
            Rate("link", 0.11898957),
            Rate("avax", 0.12355511),
            Rate("uni", 0.34224145),
            Rate("atom", 0.62676086),
            Rate("ltc", 0.020792326),
            Rate("etc", 0.12659131),
            Rate("xlm", 7.38080541),
            Rate("near", 0.79430678),
            Rate("op", 7.7837258),
            Rate("arb", 9.11977829),
            Rate("inj", 0.30318237),
            Rate("sui", 1.21417499),
            Rate("ton", 0.4394583),
        )
    )
}