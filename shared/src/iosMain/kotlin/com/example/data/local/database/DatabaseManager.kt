package com.example.data.local.database

actual object DatabaseManager {
    private lateinit var _database: AppDatabase
    actual val database: AppDatabase get() = _database

    suspend fun initDatabase() {
        val driverFactory = DriverFactory()
        _database = createDatabase(driverFactory)
    }
}