package io.github.viacheslav.chugunov.moniqo.core.repository

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import kotlinx.coroutines.flow.Flow

interface SettingStorageRepository {
    fun getAppLanguage(): Flow<AppLanguage>

    suspend fun setAppLanguage(language: AppLanguage)

    fun getAppTheme(): Flow<AppTheme>

    suspend fun setAppTheme(theme: AppTheme)

    fun getDealRanges(): Flow<DealRanges>

    suspend fun setDealRanges(ranges: DealRanges)

    suspend fun resetDealRanges()

    fun getBaseRatesCurrency(): Flow<Currency>

    suspend fun setBaseRatesCurrency(currency: Currency)

    fun getRecentCurrencies(): Flow<List<String>>

    suspend fun addRecentCurrency(code: String)
}
