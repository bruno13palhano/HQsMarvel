package com.bruno13palhano.data.local.database

import com.bruno13palhano.cache.AppDatabase

class DatabaseFactory(private val driverFactory: DriverFactory) {
    fun createDatabase(): AppDatabase {
        return AppDatabase(driver = driverFactory.createDriver())
    }
}