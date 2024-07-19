package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

interface ComicsLocalData {
    suspend fun save(comic: Comic)

    suspend fun remove(id: Long)

    suspend fun removeAll()

    fun getFavoriteComics(): Flow<List<Comic>>

    fun getComics(): Flow<List<Comic>>

    suspend fun getAll(): List<Comic>

    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    suspend fun getComicsLimited(offset: Long, limit: Long): List<Comic>
}