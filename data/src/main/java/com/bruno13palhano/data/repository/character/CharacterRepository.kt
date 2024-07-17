package com.bruno13palhano.data.repository.character

import androidx.paging.PagingData
import com.bruno13palhano.data.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(id: Long): Flow<PagingData<Character>>
}