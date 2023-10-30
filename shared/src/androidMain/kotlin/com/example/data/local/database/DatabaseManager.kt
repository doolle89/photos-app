package com.example.data.local.database

import android.content.Context

actual object DatabaseManager {
    private lateinit var _database: AppDatabase
    actual val database: AppDatabase get() = _database

    fun initDatabase(context: Context) {
        val driverFactory = DriverFactory(context)
        _database = createDatabase(driverFactory)
    }
}