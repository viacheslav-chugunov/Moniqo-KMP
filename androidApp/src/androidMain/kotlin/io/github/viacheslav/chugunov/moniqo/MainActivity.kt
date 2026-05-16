package io.github.viacheslav.chugunov.moniqo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import io.github.viacheslav.chugunov.moniqo.core.usecase.GetSplashReadyFlowUseCase
import io.github.viacheslav.chugunov.moniqo.screen.App
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val getSplashReadyFlow: GetSplashReadyFlowUseCase by inject()
    private var isSplashReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            getSplashReadyFlow().collect { ready -> isSplashReady = ready }
        }
        splashScreen.setKeepOnScreenCondition { !isSplashReady }
        enableEdgeToEdge()
        setContent(content = ::App)
    }
}
