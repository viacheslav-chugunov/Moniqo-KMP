package io.github.viacheslav.chugunov.moniqo

import io.github.viacheslav.chugunov.moniqo.core.di.coreModule
import io.github.viacheslav.chugunov.moniqo.network.di.networkModule
import io.github.viacheslav.chugunov.moniqo.storage.di.storageModule
import io.github.viacheslav.chugunov.moniqo.storage.platform.DatabaseDriverFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module

object KoinHelper {
    fun doInitKoin() {
        startKoin {
            modules(
                module {
                    factory { DatabaseDriverFactory() }
                },
                coreModule,
                networkModule,
                storageModule,
            )
        }
    }
}
