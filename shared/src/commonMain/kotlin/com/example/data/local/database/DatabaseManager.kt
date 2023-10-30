package com.example.data.local.database

expect object DatabaseManager {
    val database: AppDatabase
}

fun createDatabase(driverFactory: DriverFactory): AppDatabase {
    val driver = driverFactory.createDriver()
    return AppDatabase(driver)
}
