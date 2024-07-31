package com.bruno13palhano.data.local.data.mediator

import androidx.room.withTransaction
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.ComicOffset
import com.bruno13palhano.data.model.RemoteKeys
import com.bruno13palhano.data.remote.model.comics.ComicNet
import javax.inject.Inject

internal class DefaultMediatorComicLocalData
    @Inject
    constructor(
        private val database: HQsMarvelDatabase
    ) : MediatorComicLocalData {
        override suspend fun insertAll(
            page: Int,
            nextOffset: Int,
            endOfPaginationReached: Boolean,
            isRefresh: Boolean,
            comics: List<ComicNet>
        ) {
            database.withTransaction {
                if (isRefresh) {
                    database.comicsDao.clearComics()
                }

                val prevKey = if (page > 1) page - 1 else null
                val next = if (endOfPaginationReached) null else page + 1

                val remoteKeys: MutableList<RemoteKeys> = mutableListOf()
                val comicList: MutableList<Comic> = mutableListOf()
                val characterList: MutableList<CharacterSummary> = mutableListOf()

                comics.map { comicNet ->
                    remoteKeys.add(
                        RemoteKeys(
                            comicId = comicNet.id,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = next,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                    comicList.add(
                        Comic(
                            comicId = comicNet.id,
                            title = comicNet.title ?: "",
                            description = comicNet.description ?: "",
                            thumbnail = comicNet.thumbnail?.path + "." + comicNet.thumbnail?.extension,
                            page = page,
                            isFavorite = false
                        )
                    )
                    comicNet.characters.items.map { characterSummary ->
                        characterList.add(
                            CharacterSummary(
                                id = getCharacterIdFromResourceURI(characterSummary.resourceURI),
                                comicId = comicNet.id,
                                resourceURI = characterSummary.resourceURI,
                                name = characterSummary.name,
                                role = characterSummary.role
                            )
                        )
                    }
                }

                database.comicOffsetDao.insert(
                    comicOffset =
                        ComicOffset(
                            id = 1L,
                            lastOffset = nextOffset
                        )
                )
                database.comicsDao.insertAll(comics = comicList)
                database.remoteKeysDao.insertAll(remoteKeys = remoteKeys)
                database.characterSummaryDao.insertAll(characterSummary = characterList)
            }
        }

        override suspend fun insertLastOffset(lastOffset: ComicOffset) {
            database.comicOffsetDao.insert(comicOffset = lastOffset)
        }

        override suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys? {
            return database.remoteKeysDao.getRemoteKeyByComicId(comicId = comicId)
        }

        override suspend fun getCreationTime(): Long? {
            return database.remoteKeysDao.getCreationTime()
        }

        override suspend fun getLastOffset(): Int? {
            return database.comicOffsetDao.getLastOffset()
        }

        private fun getCharacterIdFromResourceURI(resourceURI: String?): Long {
            return try {
                resourceURI?.split("/")?.last()?.toLong() ?: 0L
            } catch (e: Exception) {
                0L
            }
        }
    }