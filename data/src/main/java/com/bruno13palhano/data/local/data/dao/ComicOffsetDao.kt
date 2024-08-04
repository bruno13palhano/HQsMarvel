package com.bruno13palhano.data.local.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.local.data.ComicOffsetLocalData
import com.bruno13palhano.data.model.ComicOffset

@Dao
interface ComicOffsetDao : ComicOffsetLocalData {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertComicOffset(comicOffset: ComicOffset)

    @Query("SELECT lastOffset FROM ComicOffset ORDER BY id DESC LIMIT 1")
    override suspend fun getLastOffset(): Int?
}