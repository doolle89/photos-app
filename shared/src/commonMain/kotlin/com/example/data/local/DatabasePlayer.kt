package com.example.data.local

import app.cash.sqldelight.db.SqlDriver
import com.example.data.local.database.AppDatabase
import com.example.data.local.database.DriverFactory
import com.example.data.local.database.HockeyPlayer
import com.example.data.local.database.PlayerQueries

suspend fun createDatabase(driverFactory: DriverFactory): AppDatabase {
    val driver = driverFactory.createDriver()
    val database = AppDatabase(driver)

    // Do more work with the database (see below).
    doDatabaseThings(driver)

    return database
}

suspend fun doDatabaseThings(driver: SqlDriver) {
    val database = AppDatabase(driver)
    val playerQueries: PlayerQueries = database.playerQueries

    println(playerQueries.selectAll().executeAsList())
    // [HockeyPlayer(15, "Ryan Getzlaf")]

    playerQueries.insert(player_number = 10, full_name = "Corey Perry")
    println(playerQueries.selectAll().executeAsList())
    // [HockeyPlayer(15, "Ryan Getzlaf"), HockeyPlayer(10, "Corey Perry")]

    val player = HockeyPlayer(10, "Ronald McDonald")
    playerQueries.insertFullPlayerObject(player)
}