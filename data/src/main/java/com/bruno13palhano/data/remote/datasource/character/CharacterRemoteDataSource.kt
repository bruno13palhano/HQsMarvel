package com.bruno13palhano.data.remote.datasource.character

import com.bruno13palhano.data.model.Character

internal interface CharacterRemoteDataSource {
    suspend fun getCharacters(
        id: Long,
        offset: Int,
        limit: Int
    ): List<Character>
}