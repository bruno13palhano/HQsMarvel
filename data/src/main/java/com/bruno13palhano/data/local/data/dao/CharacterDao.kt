package com.bruno13palhano.data.local.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.local.data.CharacterLocalData
import com.bruno13palhano.data.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao : CharacterLocalData {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insert(character: Character)

    @Query("SELECT * FROM Character WHERE id = :id")
    override fun getCharacter(id: Long): Flow<Character?>

    @Query("SELECT EXISTS(SELECT * FROM Character WHERE id = :id)")
    override suspend fun characterExists(id: Long): Boolean
}