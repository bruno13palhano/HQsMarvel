package com.bruno13palhano.data.local.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ComicsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(comic: Comic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(comics: List<Comic>)

    @Query("SELECT * FROM Comics ORDER BY page")
    fun getAll(): PagingSource<Int, Comic>

    @Query("DELETE FROM Comics WHERE isFavorite = 0")
    suspend fun clearComics()

    @Query("SELECT * FROM Comics")
    suspend fun getComics(): List<Comic>

    @Query("SELECT * FROM Comics WHERE isFavorite = 1")
    fun getFavoriteComics(): Flow<List<Comic>>

    @Query("SELECT * FROM Comics WHERE comicId = :comicId AND page = :page AND isFavorite = 1")
    fun getFavoriteComicById(
        comicId: Long,
        page: Int
    ): Flow<Comic>

    @Query("UPDATE Comics SET isFavorite = :isFavorite WHERE comicId = :comicId AND page = :page")
    suspend fun updateFavorite(
        comicId: Long,
        page: Int,
        isFavorite: Boolean
    )
}