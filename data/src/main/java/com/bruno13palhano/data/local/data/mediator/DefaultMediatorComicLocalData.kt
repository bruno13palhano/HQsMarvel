package com.bruno13palhano.data.local.data.mediator

import androidx.room.withTransaction
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.RemoteKeys
import javax.inject.Inject

internal class DefaultMediatorComicLocalData
    @Inject
    constructor(
        private val database: HQsMarvelDatabase
    ) : MediatorComicLocalData {
        override suspend fun insertAll(
            page: Int,
            endOfPaginationReached: Boolean,
            isRefresh: Boolean,
            comics: List<Comic>
        ) {
            database.withTransaction {
                if (isRefresh) {
                    database.comicsDao.getComics().map {
                        if (it.isFavorite) {
                            database.remoteKeysDao.deleteById(comicId = it.comicId)
                        }
                    }
                    database.comicsDao.clearComics()
                }

                val prevKey = if (page > 1) page - 1 else null
                val next = if (endOfPaginationReached) null else page + 1
                val remoteKeys =
                    comics.map {
                        RemoteKeys(
                            comicId = it.comicId,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = next,
                            createdAt = System.currentTimeMillis()
                        )
                    }

                database.remoteKeysDao.insertAll(remoteKeys = remoteKeys)
                database.comicsDao.insertAll(comics = comics.map { it.copy(page = page) })
            }
        }

        override suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys? {
            return database.remoteKeysDao.getRemoteKeyByComicId(comicId = comicId)
        }

        override suspend fun getCreationTime(): Long? {
            return database.remoteKeysDao.getCreationTime()
        }

        override suspend fun getCurrentPage(): Int? {
            return database.remoteKeysDao.getCurrentPage()
        }
    }