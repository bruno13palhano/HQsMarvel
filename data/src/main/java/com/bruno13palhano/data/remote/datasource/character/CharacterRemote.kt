package com.bruno13palhano.data.remote.datasource.character

import com.bruno13palhano.data.model.Character

internal interface CharacterRemote {
    suspend fun getCharacter(id: Long): Character
}