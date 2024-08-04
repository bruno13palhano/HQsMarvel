package com.bruno13palhano.data.local.data.mediator

import androidx.room.withTransaction
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.ComicOffset
import com.bruno13palhano.data.remote.model.comics.ComicNet
import javax.inject.Inject

internal class DefaultMediatorComicLocalData
    @Inject
    constructor(
        private val database: HQsMarvelDatabase
    ) : MediatorComicLocalData {
        override suspend fun insertComicsAndRelatedData(
            page: Int,
            nextOffset: Int,
            endOfPaginationReached: Boolean,
            isRefresh: Boolean,
            comicNets: List<ComicNet>
        ) {
            database.withTransaction {
                if (isRefresh) {
                    database.comicsDao.clearComics()
                }

                val nextPage = if (endOfPaginationReached) null else page + 1

                val comicList: MutableList<Comic> = mutableListOf()
                val characterList: MutableList<CharacterSummary> = mutableListOf()

                comicNets.map { comicNet ->
                    comicList.add(
                        Comic(
                            id = comicNet.id,
                            title = comicNet.title ?: "",
                            description = comicNet.description ?: "",
                            thumbnail = comicNet.thumbnail?.path + "." + comicNet.thumbnail?.extension,
                            page = page,
                            nextPage = nextPage,
                            isFavorite = false,
                            createdAt = System.currentTimeMillis()
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

                database.comicOffsetDao.insertComicOffset(
                    comicOffset =
                        ComicOffset(
                            id = 1L,
                            lastOffset = nextOffset
                        )
                )
                database.comicsDao.insertComics(comics = comicList)
                database.characterSummaryDao.insertCharactersSummary(characterSummary = characterList)
            }
        }

        override suspend fun insertLastOffset(lastOffset: ComicOffset) {
            database.comicOffsetDao.insertComicOffset(comicOffset = lastOffset)
        }

        override suspend fun getNextPageByComicId(comicId: Long): Int? {
            return database.comicsDao.getNextPageByComicId(id = comicId)
        }

        override suspend fun getCreationTime(): Long? {
            return database.comicsDao.getCreationTime()
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