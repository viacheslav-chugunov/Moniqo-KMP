package io.github.viacheslav.chugunov.moniqo.test.mock.repository

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SettingStorageRepositoryMock(
    appLanguage: AppLanguage = AppLanguage.SYSTEM,
    appTheme: AppTheme = AppTheme.SYSTEM,
    dealRanges: DealRanges = DealRanges(5, 10),
    baseRatesCurrency: Currency = Currency.of("eur"),
    recentCurrencies: List<String> = listOf("EUR", "USD"),
) : SettingStorageRepository {
    val appLanguageFlow = MutableStateFlow(appLanguage)
    val appThemeFlow = MutableStateFlow(appTheme)
    val dealRangesFlow = MutableStateFlow(dealRanges)
    val baseRatesCurrencyFlow = MutableStateFlow(baseRatesCurrency)
    val recentCurrenciesFlow = MutableStateFlow(recentCurrencies)

    var savedLanguage: AppLanguage? = null
    var savedTheme: AppTheme? = null
    var savedDealRanges: DealRanges? = null
    var savedBaseCurrency: Currency? = null
    var addedCurrencyCode: String? = null
    var resetDealRangesCalled: Boolean = false

    override fun getAppLanguage(): Flow<AppLanguage> = appLanguageFlow

    override suspend fun setAppLanguage(language: AppLanguage) {
        savedLanguage = language
    }

    override fun getAppTheme(): Flow<AppTheme> = appThemeFlow

    override suspend fun setAppTheme(theme: AppTheme) {
        savedTheme = theme
    }

    override fun getDealRanges(): Flow<DealRanges> = dealRangesFlow

    override suspend fun setDealRanges(ranges: DealRanges) {
        savedDealRanges = ranges
    }

    override suspend fun resetDealRanges() {
        resetDealRangesCalled = true
    }

    override fun getBaseRatesCurrency(): Flow<Currency> = baseRatesCurrencyFlow

    override suspend fun setBaseRatesCurrency(currency: Currency) {
        savedBaseCurrency = currency
    }

    override fun getRecentCurrencies(): Flow<List<String>> = recentCurrenciesFlow

    override suspend fun addRecentCurrency(code: String) {
        addedCurrencyCode = code
    }
}
