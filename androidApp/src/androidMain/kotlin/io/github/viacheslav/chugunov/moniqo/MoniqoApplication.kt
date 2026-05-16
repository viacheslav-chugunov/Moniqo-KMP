package io.github.viacheslav.chugunov.moniqo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.core.di.coreModule
import io.github.viacheslav.chugunov.moniqo.core.model.AppLanguage
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetAppLanguageFlowUseCase
import io.github.viacheslav.chugunov.moniqo.di.appModule
import io.github.viacheslav.chugunov.moniqo.network.di.networkModule
import io.github.viacheslav.chugunov.moniqo.storage.di.storageModule
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.di.chooseCurrencyModule
import io.github.viacheslav.chugunov.moniqo.ui.home.di.homeModule
import io.github.viacheslav.chugunov.moniqo.ui.rates.di.ratesModule
import io.github.viacheslav.chugunov.moniqo.ui.settings.di.settingsModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class MoniqoApplication : Application(), KoinComponent {
    private val getAppLanguageFlow: GetAppLanguageFlowUseCase by inject()
    private val coroutineDispatchers: CoroutineDispatchers by inject()
    private val appScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoniqoApplication)
            modules(
                appModule,
                coreModule,
                networkModule,
                storageModule,
                homeModule,
                ratesModule,
                chooseCurrencyModule,
                settingsModule,
            )
        }
        collectAppLanguage()
    }

    private fun collectAppLanguage() {
        appScope.launch(coroutineDispatchers.main) {
            getAppLanguageFlow().collectLatest { language ->
                val tag = when (language) {
                    AppLanguage.SYSTEM -> ""
                    AppLanguage.ENGLISH -> "en"
                    AppLanguage.LATVIAN -> "lv"
                    AppLanguage.RUSSIAN -> "ru"
                }
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
            }
        }
    }
}
