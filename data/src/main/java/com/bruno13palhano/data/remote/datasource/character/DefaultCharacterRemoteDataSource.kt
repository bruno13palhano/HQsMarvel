package com.bruno13palhano.data.remote.datasource.character

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.remote.Service
import javax.inject.Inject

internal class DefaultCharacterRemoteDataSource
    @Inject
    constructor(
        private val service: Service
    ) : CharacterRemoteDataSource {
        override suspend fun getCharacters(
            id: Long,
            offset: Int,
            limit: Int
        ): List<Character> {
            return service.getCharacters(id = id, offset = offset, limit = limit).data.results.map {
                Character(
                    id = it.id,
                    name = it.name ?: "",
                    description = it.description ?: "",
                    thumbnail = (it.thumbnail?.path ?: "") + "." + (it.thumbnail?.extension ?: "")
                )
            }
        }
    }