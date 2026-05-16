package io.github.viacheslav.chugunov.moniqo.storage.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.github.viacheslav.chugunov.moniqo.core.di.CoroutineDispatchers
import io.github.viacheslav.chugunov.moniqo.storage.db.AppDatabase
import io.github.viacheslav.chugunov.moniqo.storage.db.SettingEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal interface SettingLocalDataSource {
    fun get(name: String): Flow<String?>
    suspend fun set(name: String, value: String)
    fun getAllAsFlow(): Flow<List<SettingEntity>>
}

internal class SettingLocalDataSourceImpl(
    database: AppDatabase,
    private val dispatchers: CoroutineDispatchers,
) : SettingLocalDataSource {
    private val ratePairEntityQueries = database.settingEntityQueries

    override fun get(
        name: String,
    ): Flow<String?> =
        ratePairEntityQueries.getSettingByName(name).asFlow().mapToOneOrNull(dispatchers.io).map { it?.value_ }

    override suspend fun set(name: String, value: String) = withContext(dispatchers.io) {
        ratePairEntityQueries.insertSetting(name, value)
    }

    override fun getAllAsFlow(): Flow<List<SettingEntity>> =
        ratePairEntityQueries.getAllSettings().asFlow().mapToList(dispatchers.io)

}
