package com.bruno13palhano.data.local.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ComicsDao : ComicLocalData {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insert(comic: Comic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(comics: List<Comic>)

    @Query("SELECT * FROM Comics ORDER BY page")
    override fun getAll(): PagingSource<Int, Comic>

    @Query("DELETE FROM Comics WHERE isFavorite = 0")
    override suspend fun clearComics()

    @Query("SELECT * FROM Comics")
    override suspend fun getComics(): List<Comic>

    @Query("SELECT * FROM Comics WHERE isFavorite = 1")
    override fun getFavoriteComics(): Flow<List<Comic>>

    @Query("SELECT * FROM Comics WHERE comicId = :comicId AND isFavorite = 1")
    override fun getFavoriteComicById(comicId: Long): Flow<Comic>

    @Query("UPDATE Comics SET isFavorite = :isFavorite WHERE comicId = :comicId")
    override suspend fun updateFavorite(
        comicId: Long,
        isFavorite: Boolean
    )
}