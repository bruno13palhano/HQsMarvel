package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.CharacterLocalData
import com.bruno13palhano.data.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockCharacterLocalData : CharacterLocalData {
    private val characters = mutableListOf<Character>()

    override suspend fun insert(character: Character) {
        characters.add(character)
    }

    override fun getCharacter(id: Long): Flow<Character?> {
        return flowOf(characters.find { it.id == id })
    }

    override suspend fun characterExists(id: Long): Boolean {
        return characters.any { it.id == id }
    }
}