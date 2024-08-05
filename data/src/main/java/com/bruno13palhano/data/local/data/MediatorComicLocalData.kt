package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.ComicOffset
import com.bruno13palhano.data.remote.model.Response
import com.bruno13palhano.data.remote.model.comics.ComicNet

internal interface MediatorComicLocalData {
    /**
     * Persist [Comic]s, [ComicOffset] and [CharacterSummary]s.
     *
     * [Comic]s and [CharacterSummary]s are mapped from [ComicNet]
     *
     * @param page The page number of the comic.
     *
     * @param nextOffset The next offset wrapped into a [ComicOffset] to be persisted
     * for further requests.
     *
     * @param endOfPaginationReached Whether the end of pagination has been reached.
     *
     * @param isRefresh Whether the data is being refreshed.
     *
     * @param comicNets The list of [ComicNet]s to be mapped to [Comic]s and
     * [CharacterSummary]s and persisted.
     */
    suspend fun insertComicsAndRelatedData(
        page: Int,
        nextOffset: Int,
        endOfPaginationReached: Boolean,
        isRefresh: Boolean,
        comicNets: Response<ComicNet>
    )

    /**
     * Persist [ComicOffset].
     *
     * @param lastOffset The [ComicOffset] to be inserted.
     */
    suspend fun insertLastOffset(lastOffset: ComicOffset)

    /**
     * Get the next page number for a given [Comic] by its [comicId].
     *
     * @param comicId The [Comic] id.
     */
    suspend fun getNextPageByComicId(comicId: Long): Int?

    /**
     * Get the creation time of the last [Comic] persisted.
     */
    suspend fun getCreationTime(): Long?

    /**
     * Get the last [ComicOffset] value persisted.
     */
    suspend fun getLastOffset(): Int?
}