package com.bruno13palhano.data.local.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.model.CharacterSummary

@Dao
internal interface CharacterSummaryDao : CharacterSummaryLocalData {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertCharacterSummary(characterSummary: CharacterSummary)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertCharactersSummary(characterSummary: List<CharacterSummary>)

    @Query("SELECT * FROM CharacterSummary WHERE comicId = :comicId LIMIT :offset, :limit")
    override suspend fun getCharacterSummaryByComicId(
        comicId: Long,
        offset: Int,
        limit: Int
    ): List<CharacterSummary>
}