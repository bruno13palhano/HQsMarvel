package com.bruno13palhano.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.data.local.data.ComicsDao
import com.bruno13palhano.data.local.data.FavoriteComicsDao
import com.bruno13palhano.data.local.data.RemoteKeysDao
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.FavoriteComic
import com.bruno13palhano.data.model.RemoteKeys

@Database(
    entities = [
        Comic::class,
        RemoteKeys::class,
        FavoriteComic::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class HQsMarvelDatabase : RoomDatabase() {
    abstract val comicsDao: ComicsDao
    abstract val remoteKeysDao: RemoteKeysDao
    abstract val favoriteComicsDao: FavoriteComicsDao
}