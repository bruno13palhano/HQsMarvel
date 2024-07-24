package com.bruno13palhano.data.repository.charactersummary

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.local.di.DefaultCharacterSummary
import com.bruno13palhano.data.model.CharacterSummary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultCharacterSummaryRepository
    @Inject
    constructor(
        @DefaultCharacterSummary private val characterSummaryLocalData: CharacterSummaryLocalData
    ) : CharacterSummaryRepository {
        override fun getCharactersSummary(comicId: Long): Flow<PagingData<CharacterSummary>> {
            return Pager(
                config = PagingConfig(pageSize = 15, prefetchDistance = 1),
                pagingSourceFactory = {
                    CharacterSummaryPagingSource(
                        characterSummaryLocalData = characterSummaryLocalData,
                        comicId = comicId,
                        offset = 0,
                        limit = 15
                    )
                }
            ).flow
        }
    }