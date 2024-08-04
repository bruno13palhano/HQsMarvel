package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.CharacterSummary

internal interface CharacterSummaryLocalData {
    suspend fun insertCharacterSummary(characterSummary: CharacterSummary)

    suspend fun insertCharactersSummary(characterSummary: List<CharacterSummary>)

    suspend fun getCharacterSummaryByComicId(
        comicId: Long,
        offset: Int,
        limit: Int
    ): List<CharacterSummary>
}