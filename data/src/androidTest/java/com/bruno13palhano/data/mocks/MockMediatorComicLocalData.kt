package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.local.data.ComicOffsetLocalData
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.data.RemoteKeysLocalData
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.ComicOffset
import com.bruno13palhano.data.model.RemoteKeys

internal class MockMediatorComicLocalData(
    private val comicLocalData: ComicLocalData,
    private val remoteKeysLocalData: RemoteKeysLocalData,
    private val comicOffsetLocalData: ComicOffsetLocalData,
    private val characterSummaryLocalData: CharacterSummaryLocalData
) : MediatorComicLocalData {
    override suspend fun insertAll(
        page: Int,
        nextOffset: Int,
        endOfPaginationReached: Boolean,
        isRefresh: Boolean,
        comicNets: List<com.bruno13palhano.data.remote.model.comics.ComicNet>
    ) {
        if (isRefresh) {
            comicLocalData.getComics().forEach { comic ->
                if (!comic.isFavorite) {
                    remoteKeysLocalData.deleteById(comicId = comic.comicId)
                }
            }
            comicLocalData.clearComics()
        }

        val prevKey = if (page > 1) page - 1 else null
        val next = if (endOfPaginationReached) null else page + 1

        val remoteKeys: MutableList<RemoteKeys> = mutableListOf()
        val comicList: MutableList<Comic> = mutableListOf()
        val characterList: MutableList<CharacterSummary> = mutableListOf()

        comicNets.map { comicNet ->
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

        comicOffsetLocalData.insert(
            comicOffset =
                ComicOffset(
                    id = 1L,
                    lastOffset = nextOffset
                )
        )
        comicLocalData.insertAll(comics = comicList)
        remoteKeysLocalData.insertAll(remoteKeys = remoteKeys)
        characterSummaryLocalData.insertAll(characterSummary = characterList)
    }

    override suspend fun insertLastOffset(lastOffset: ComicOffset) {
        comicOffsetLocalData.insert(comicOffset = lastOffset)
    }

    override suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys? {
        return remoteKeysLocalData.getRemoteKeyByComicId(comicId = comicId)
    }

    override suspend fun getCreationTime(): Long? {
        return remoteKeysLocalData.getCreationTime()
    }

    override suspend fun getLastOffset(): Int? {
        return comicOffsetLocalData.getLastOffset()
    }

    private fun getCharacterIdFromResourceURI(resourceURI: String?): Long {
        return resourceURI?.split("/")?.last()?.toLong() ?: 0L
    }
}