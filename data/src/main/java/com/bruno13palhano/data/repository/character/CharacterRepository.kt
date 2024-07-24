package com.bruno13palhano.data.repository.character

import com.bruno13palhano.data.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacter(id: Long): Flow<Character>
}