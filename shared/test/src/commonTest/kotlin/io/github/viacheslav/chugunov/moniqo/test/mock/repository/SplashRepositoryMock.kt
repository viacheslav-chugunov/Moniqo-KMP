package io.github.viacheslav.chugunov.moniqo.test.mock.repository

import io.github.viacheslav.chugunov.moniqo.core.repository.SplashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SplashRepositoryMock : SplashRepository {
    private val flow = MutableStateFlow(false)
    var setReadyCalled: Boolean = false

    override fun isReady(): Flow<Boolean> = flow

    override fun setReady() {
        setReadyCalled = true
        flow.value = true
    }
}
