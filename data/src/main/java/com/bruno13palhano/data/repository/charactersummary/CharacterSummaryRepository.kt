package com.bruno13palhano.data.repository.charactersummary

import androidx.paging.PagingData
import com.bruno13palhano.data.model.CharacterSummary
import kotlinx.coroutines.flow.Flow

interface CharacterSummaryRepository {
    fun getCharactersSummary(comicId: Long): Flow<PagingData<CharacterSummary>>
}