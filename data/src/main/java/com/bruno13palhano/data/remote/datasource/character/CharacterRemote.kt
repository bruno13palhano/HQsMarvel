package com.bruno13palhano.data.remote.datasource.character

import com.bruno13palhano.data.remote.model.Response
import com.bruno13palhano.data.remote.model.character.CharacterNet

internal interface CharacterRemote {
    suspend fun getCharacter(id: Long): Response<CharacterNet>
}