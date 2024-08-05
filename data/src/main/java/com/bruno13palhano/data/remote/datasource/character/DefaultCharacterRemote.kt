package com.bruno13palhano.data.remote.datasource.character

import com.bruno13palhano.data.remote.Service
import com.bruno13palhano.data.remote.model.Response
import com.bruno13palhano.data.remote.model.character.CharacterNet
import javax.inject.Inject

internal class DefaultCharacterRemote
    @Inject
    constructor(
        private val service: Service
    ) : CharacterRemote {
        override suspend fun getCharacter(id: Long): Response<CharacterNet> {
            return service.getCharacter(id = id)
        }
    }