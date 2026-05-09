package io.github.viacheslav.chugunov.moniqo.storage.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.github.viacheslav.chugunov.moniqo.storage.db.AppDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver =
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            .also { AppDatabase.Schema.create(it) }
}
