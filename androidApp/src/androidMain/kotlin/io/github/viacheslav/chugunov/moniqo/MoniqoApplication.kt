package io.github.viacheslav.chugunov.moniqo

import android.app.Application
import io.github.viacheslav.chugunov.moniqo.core.di.coreModule
import io.github.viacheslav.chugunov.moniqo.di.appModule
import io.github.viacheslav.chugunov.moniqo.network.di.networkModule
import io.github.viacheslav.chugunov.moniqo.storage.di.storageModule
import io.github.viacheslav.chugunov.moniqo.ui.choosecurrency.di.chooseCurrencyModule
import io.github.viacheslav.chugunov.moniqo.ui.home.di.homeModule
import io.github.viacheslav.chugunov.moniqo.ui.rates.di.ratesModule
import io.github.viacheslav.chugunov.moniqo.ui.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoniqoApplication : Application() {
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
    }
}
