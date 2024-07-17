package com.bruno13palhano.data.repository.character

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.remote.datasource.character.CharacterRemoteDataSource
import com.bruno13palhano.data.remote.di.DefaultCharacterRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultCharacterRepository
    @Inject
    constructor(
        @DefaultCharacterRemote private val characterRemoteDataSource: CharacterRemoteDataSource
    ) : CharacterRepository {
        override fun getCharacters(id: Long): Flow<PagingData<Character>> {
            return Pager(
                config = PagingConfig(pageSize = 15, prefetchDistance = 1),
                pagingSourceFactory = {
                    CharacterPagingSource(
                        characterRemoteDataSource = characterRemoteDataSource,
                        id = id,
                        offset = 0,
                        limit = 15
                    )
                }
            ).flow
        }
    }