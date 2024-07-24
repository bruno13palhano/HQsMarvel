package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.Character
import kotlinx.coroutines.flow.Flow

internal interface CharacterLocalData {
    suspend fun insert(character: Character)

    fun getCharacter(id: Long): Flow<Character>

    suspend fun characterExists(id: Long): Boolean
}