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
    override suspend fun insertComic(comic: Comic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertComics(comics: List<Comic>)

    @Query("SELECT * FROM Comics ORDER BY page")
    override fun getComicsPaging(): PagingSource<Int, Comic>

    @Query("DELETE FROM Comics WHERE isFavorite = 0")
    override suspend fun clearComics()

    @Query("SELECT * FROM Comics")
    override suspend fun getComics(): List<Comic>

    @Query("SELECT * FROM Comics WHERE isFavorite = 1")
    override fun getFavoriteComics(): Flow<List<Comic>>

    @Query("SELECT * FROM Comics WHERE id = :comicId AND isFavorite = 1")
    override fun getFavoriteComicById(comicId: Long): Flow<Comic>

    @Query("SELECT Comics.nextPage FROM Comics WHERE id = :id")
    override suspend fun getNextPageByComicId(id: Long): Int?

    @Query("SELECT Comics.createdAt FROM Comics ORDER BY createdAt DESC LIMIT 1")
    override suspend fun getCreationTime(): Long?

    @Query("UPDATE Comics SET isFavorite = :isFavorite WHERE id = :comicId")
    override suspend fun updateFavorite(
        comicId: Long,
        isFavorite: Boolean
    )
}