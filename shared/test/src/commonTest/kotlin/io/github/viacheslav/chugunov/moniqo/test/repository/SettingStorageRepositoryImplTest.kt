@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.github.viacheslav.chugunov.moniqo.test.repository

import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.model.AppTheme
import io.github.viacheslav.chugunov.moniqo.core.model.Currency
import io.github.viacheslav.chugunov.moniqo.core.model.DealRanges
import io.github.viacheslav.chugunov.moniqo.storage.repository.SettingStorageRepositoryImpl
import io.github.viacheslav.chugunov.moniqo.test.mock.datasource.SettingLocalDataSourceMock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingStorageRepositoryImplTest {
    // ── Language ──────────────────────────────────────────────────────────

    @Test
    fun `getAppLanguage returns SYSTEM by default`() =
        runTest {
            assertEquals(AppLanguage.SYSTEM, makeRepo().getAppLanguage().first())
        }

    @Test
    fun `getAppLanguage returns stored value`() =
        runTest {
            val repo = makeRepo()
            repo.setAppLanguage(AppLanguage.ENGLISH)

            assertEquals(AppLanguage.ENGLISH, repo.getAppLanguage().first())
        }

    // ── Theme ─────────────────────────────────────────────────────────────

    @Test
    fun `getAppTheme returns SYSTEM by default`() =
        runTest {
            assertEquals(AppTheme.SYSTEM, makeRepo().getAppTheme().first())
        }

    @Test
    fun `getAppTheme returns stored value`() =
        runTest {
            val repo = makeRepo()
            repo.setAppTheme(AppTheme.DARK)

            assertEquals(AppTheme.DARK, repo.getAppTheme().first())
        }

    // ── Deal ranges ───────────────────────────────────────────────────────

    @Test
    fun `getDealRanges returns defaults`() =
        runTest {
            assertEquals(DealRanges(5, 10), makeRepo().getDealRanges().first())
        }

    @Test
    fun `getDealRanges returns custom values after setDealRanges`() =
        runTest {
            val repo = makeRepo()
            repo.setDealRanges(DealRanges(3, 7))

            assertEquals(DealRanges(3, 7), repo.getDealRanges().first())
        }

    @Test
    fun `resetDealRanges restores defaults`() =
        runTest {
            val repo = makeRepo()
            repo.setDealRanges(DealRanges(1, 2))
            repo.resetDealRanges()

            assertEquals(DealRanges(5, 10), repo.getDealRanges().first())
        }

    // ── Base rates currency ───────────────────────────────────────────────

    @Test
    fun `getBaseRatesCurrency returns EUR by default`() =
        runTest {
            assertEquals(Currency.of("eur"), makeRepo().getBaseRatesCurrency().first())
        }

    @Test
    fun `getBaseRatesCurrency returns stored value`() =
        runTest {
            val repo = makeRepo()
            repo.setBaseRatesCurrency(Currency.of("usd"))

            assertEquals(Currency.of("usd"), repo.getBaseRatesCurrency().first())
        }

    // ── Recent currencies ─────────────────────────────────────────────────

    @Test
    fun `getRecentCurrencies returns EUR USD by default`() =
        runTest {
            assertEquals(listOf("EUR", "USD"), makeRepo().getRecentCurrencies().first())
        }

    @Test
    fun `addRecentCurrency prepends new code to list`() =
        runTest {
            val repo = makeRepo()
            repo.addRecentCurrency("GBP")

            assertEquals("GBP", repo.getRecentCurrencies().first().first())
        }

    @Test
    fun `addRecentCurrency moves duplicate to front`() =
        runTest {
            val repo = makeRepo()
            repo.addRecentCurrency("USD")

            val result = repo.getRecentCurrencies().first()
            assertEquals("USD", result.first())
            assertEquals(1, result.count { it == "USD" })
        }

    @Test
    fun `addRecentCurrency caps list at five entries`() =
        runTest {
            val repo = makeRepo()
            listOf("A", "B", "C", "D", "E").forEach { repo.addRecentCurrency(it) }

            assertEquals(5, repo.getRecentCurrencies().first().size)
        }

    private fun makeRepo(dataSource: SettingLocalDataSourceMock = SettingLocalDataSourceMock()) =
        SettingStorageRepositoryImpl(settingDataSource = dataSource)
}
