package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.Character
import kotlinx.coroutines.flow.Flow

internal interface CharacterLocalData {
    suspend fun insertCharacter(character: Character)

    fun getCharacterById(id: Long): Flow<Character?>

    suspend fun characterExists(id: Long): Boolean
}