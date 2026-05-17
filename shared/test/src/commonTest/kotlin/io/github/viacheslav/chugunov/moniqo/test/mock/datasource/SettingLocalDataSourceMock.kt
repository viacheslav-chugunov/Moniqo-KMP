@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package io.github.viacheslav.chugunov.moniqo.test.mock.datasource

import io.github.viacheslav.chugunov.moniqo.storage.datasource.SettingLocalDataSource
import io.github.viacheslav.chugunov.moniqo.storage.db.SettingEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SettingLocalDataSourceMock : SettingLocalDataSource {
    private val settings = MutableStateFlow<Map<String, String>>(emptyMap())

    override fun get(name: String): Flow<String?> = settings.map { it[name] }

    override suspend fun set(
        name: String,
        value: String,
    ) {
        settings.value = settings.value + (name to value)
    }

    override fun getAllAsFlow(): Flow<List<SettingEntity>> = flowOf(emptyList())
}
