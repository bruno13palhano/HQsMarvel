package com.bruno13palhano.data.repository.character

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.repository.utils.CodeInfoException
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    @Throws(CodeInfoException::class)
    suspend fun getCharacter(id: Long): Flow<Character?>
}