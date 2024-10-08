package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.model.CharacterSummary

class MockCharacterSummaryLocalData(private val throwError: Boolean) : CharacterSummaryLocalData {
    private val characters = mutableListOf<CharacterSummary>()

    override suspend fun insertCharacterSummary(characterSummary: CharacterSummary) {
        characters.add(characterSummary)
    }

    override suspend fun insertCharactersSummary(characterSummary: List<CharacterSummary>) {
        characters.addAll(characterSummary)
    }

    override suspend fun getCharacterSummaryByComicId(
        comicId: Long,
        offset: Int,
        limit: Int
    ): List<CharacterSummary> {
        //  Simulate an error.
        if (throwError) throw Exception()

        return characters.filter { it.comicId == comicId }.subList(offset, limit)
    }
}