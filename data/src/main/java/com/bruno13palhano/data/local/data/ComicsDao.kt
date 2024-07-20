package com.bruno13palhano.data.local.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.model.Comic

@Dao
internal interface ComicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comics: List<Comic>)

    @Query("SELECT * FROM Comics ORDER BY page")
    fun getAll(): PagingSource<Int, Comic>

    @Query("DELETE FROM Comics")
    suspend fun clearComics()
}