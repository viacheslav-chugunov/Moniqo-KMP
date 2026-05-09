package io.github.viacheslav.chugunov.moniqo.storage.platform

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun create(): SqlDriver
}