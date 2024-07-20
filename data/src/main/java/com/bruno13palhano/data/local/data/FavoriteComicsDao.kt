package com.bruno13palhano.data.local.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.model.FavoriteComic
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteComicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteComic: FavoriteComic)

    @Query("SELECT * FROM FavoriteComics")
    fun getAll(): Flow<List<FavoriteComic>>

    @Query("DELETE FROM FavoriteComics WHERE id = :id")
    suspend fun delete(id: Long)
}