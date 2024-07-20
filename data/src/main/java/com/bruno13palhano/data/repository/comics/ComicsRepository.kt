package com.bruno13palhano.data.repository.comics

import androidx.paging.PagingData
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

interface ComicsRepository {
    fun getComics(): Flow<PagingData<Comic>>

    suspend fun addToFavorite(comic: Comic)
}