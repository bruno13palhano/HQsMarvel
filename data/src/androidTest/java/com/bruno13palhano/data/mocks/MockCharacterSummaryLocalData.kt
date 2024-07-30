package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.model.CharacterSummary

class MockCharacterSummaryLocalData : CharacterSummaryLocalData {
    private val characters = mutableListOf<CharacterSummary>()

    override suspend fun insert(characterSummary: CharacterSummary) {
        characters.add(characterSummary)
    }

    override suspend fun insertAll(characterSummary: List<CharacterSummary>) {
        characters.addAll(characterSummary)
    }

    override suspend fun getCharacterSummaryByComicId(
        comicId: Long,
        offset: Int,
        limit: Int
    ): List<CharacterSummary> {
        return characters.filter { it.comicId == comicId }.subList(offset, limit)
    }
}