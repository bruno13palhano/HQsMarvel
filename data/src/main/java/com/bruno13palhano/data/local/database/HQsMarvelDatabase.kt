package com.bruno13palhano.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.data.dao.RemoteKeysDao
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.RemoteKeys

@Database(
    entities = [
        Comic::class,
        RemoteKeys::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class HQsMarvelDatabase : RoomDatabase() {
    abstract val comicsDao: ComicsDao
    abstract val remoteKeysDao: RemoteKeysDao
}