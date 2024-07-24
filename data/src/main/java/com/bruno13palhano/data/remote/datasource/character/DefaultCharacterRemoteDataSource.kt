package com.bruno13palhano.data.remote.datasource.character

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.remote.Service
import javax.inject.Inject

internal class DefaultCharacterRemoteDataSource
    @Inject
    constructor(
        private val service: Service
    ) : CharacterRemoteDataSource {
        override suspend fun getCharacter(id: Long): Character {
            val characterNet = service.getCharacter(id = id).data.results[0]

            return Character(
                id = characterNet.id,
                name = characterNet.name,
                description = characterNet.description,
                thumbnail = characterNet.thumbnail?.path + "." + characterNet.thumbnail?.extension
            )
        }
    }