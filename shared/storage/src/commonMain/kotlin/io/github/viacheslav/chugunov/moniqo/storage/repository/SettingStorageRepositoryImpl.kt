package io.github.viacheslav.chugunov.moniqo.storage.repository

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.core.repository.SettingStorageRepository
import io.github.viacheslav.chugunov.moniqo.storage.datasource.SettingLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map

internal class SettingStorageRepositoryImpl(
    private val settingDataSource: SettingLocalDataSource
) : SettingStorageRepository {

    override fun getAppLanguage(): Flow<AppLanguage> =
        settingDataSource.get(SETTING_APP_LANGUAGE).map { value ->
            AppLanguage.entries[value?.toIntOrNull() ?: DEFAULT_APP_LANGUAGE]
        }

    override suspend fun setAppLanguage(language: AppLanguage) =
        settingDataSource.set(SETTING_APP_LANGUAGE, language.ordinal.toString())

    override fun getAppTheme(): Flow<AppTheme> =
        settingDataSource.get(SETTING_APP_THEME).map { value ->
            AppTheme.entries[value?.toIntOrNull() ?: DEFAULT_APP_THEME]
        }

    override suspend fun setAppTheme(theme: AppTheme) =
        settingDataSource.set(SETTING_APP_THEME, theme.ordinal.toString())

    override fun getDealRanges(): Flow<DealRanges> =
        combineTransform(
            settingDataSource.get(SETTING_GOOD_DEAL),
            settingDataSource.get(SETTING_MEDIUM_DEAL)
        ) { good, medium ->
            emit(
                DealRanges(
                    good = good?.toIntOrNull() ?: DEFAULT_GOOD_DEAL,
                    medium = medium?.toIntOrNull() ?: DEFAULT_MEDIUM_DEAL
                )
            )
        }

    override suspend fun setDealRanges(ranges: DealRanges) {
        settingDataSource.set(SETTING_GOOD_DEAL, ranges.good.toString())
        settingDataSource.set(SETTING_MEDIUM_DEAL, ranges.medium.toString())
    }

    override suspend fun resetDealRanges() {
        settingDataSource.set(SETTING_GOOD_DEAL, DEFAULT_GOOD_DEAL.toString())
        settingDataSource.set(SETTING_MEDIUM_DEAL, DEFAULT_MEDIUM_DEAL.toString())
    }

    override fun getBaseRatesCurrency(): Flow<Currency> =
        settingDataSource.get(SETTING_BASE_RATES_CURRENCY).map { name ->
            Currency.of(name ?: DEFAULT_BASE_RATES_CURRENCY)
        }

    override suspend fun setBaseRatesCurrency(currency: Currency) {
        settingDataSource.set(SETTING_BASE_RATES_CURRENCY, currency.name)
    }

    companion object {
        private const val SETTING_APP_LANGUAGE = "app-language"
        private const val SETTING_APP_THEME = "app-theme"
        private const val SETTING_GOOD_DEAL = "good-deal"
        private const val SETTING_MEDIUM_DEAL = "medium-deal"
        private const val SETTING_BASE_RATES_CURRENCY = "base-rates-currency"

        private val DEFAULT_APP_LANGUAGE = AppLanguage.SYSTEM.ordinal
        private val DEFAULT_APP_THEME = AppTheme.SYSTEM.ordinal
        private const val DEFAULT_GOOD_DEAL = 5
        private const val DEFAULT_MEDIUM_DEAL = 10
        private const val DEFAULT_BASE_RATES_CURRENCY = "eur"
    }
}
