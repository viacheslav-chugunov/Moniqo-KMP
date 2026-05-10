package io.github.viacheslav.chugunov.moniqo

import android.app.Application
import io.github.viacheslav.chugunov.moniqo.core.di.coreModule
import io.github.viacheslav.chugunov.moniqo.di.appModule
import io.github.viacheslav.chugunov.moniqo.network.di.networkModule
import io.github.viacheslav.chugunov.moniqo.storage.di.storageModule
import io.github.viacheslav.chugunov.moniqo.ui.home.di.homeModule
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
            )
        }
    }
}
