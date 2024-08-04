package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.local.data.ComicOffsetLocalData
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.ComicOffset

internal class MockMediatorComicLocalData(
    private val comicLocalData: ComicLocalData,
    private val comicOffsetLocalData: ComicOffsetLocalData,
    private val characterSummaryLocalData: CharacterSummaryLocalData
) : MediatorComicLocalData {
    override suspend fun insertComicsAndRelatedData(
        page: Int,
        nextOffset: Int,
        endOfPaginationReached: Boolean,
        isRefresh: Boolean,
        comicNets: List<com.bruno13palhano.data.remote.model.comics.ComicNet>
    ) {
        if (isRefresh) {
            comicLocalData.clearComics()
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

        comicOffsetLocalData.insertComicOffset(
            comicOffset =
                ComicOffset(
                    id = 1L,
                    lastOffset = nextOffset
                )
        )
        comicLocalData.insertComics(comics = comicList)
        characterSummaryLocalData.insertCharactersSummary(characterSummary = characterList)
    }

    override suspend fun insertLastOffset(lastOffset: ComicOffset) {
        comicOffsetLocalData.insertComicOffset(comicOffset = lastOffset)
    }

    override suspend fun getNextPageByComicId(comicId: Long): Int? {
        return comicLocalData.getNextPageByComicId(id = comicId)
    }

    override suspend fun getCreationTime(): Long? {
        return comicLocalData.getCreationTime()
    }

    override suspend fun getLastOffset(): Int? {
        return comicOffsetLocalData.getLastOffset()
    }

    private fun getCharacterIdFromResourceURI(resourceURI: String?): Long {
        return resourceURI?.split("/")?.last()?.toLong() ?: 0L
    }
}