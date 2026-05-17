@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.github.viacheslav.chugunov.moniqo.test.mock.datasource

import io.github.viacheslav.chugunov.moniqo.core.model.CurrencyRates
import io.github.viacheslav.chugunov.moniqo.storage.datasource.CurrencyLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.model.CurrencyRatesRecord
import io.github.viacheslav.chugunov.moniqo.test.mock.model.currencyRatesRecordMock

class CurrencyLocalDataSourceMock(
    private val record: CurrencyRatesRecord? = currencyRatesRecordMock,
) : CurrencyLocalDataSource {
    var savedRates: CurrencyRates? = null

    override suspend fun save(rates: CurrencyRates) {
        savedRates = rates
    }

    override suspend fun get() = record
}
