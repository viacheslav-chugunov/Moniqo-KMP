package io.github.viacheslav.chugunov.moniqo.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SplashRepository {
    fun isReady(): Flow<Boolean>

    fun setReady()
}

internal class SplashRepositoryImpl : SplashRepository {
    private val _isReady = MutableStateFlow(false)

    override fun isReady(): Flow<Boolean> = _isReady.asStateFlow()

    override fun setReady() {
        _isReady.value = true
    }
}
