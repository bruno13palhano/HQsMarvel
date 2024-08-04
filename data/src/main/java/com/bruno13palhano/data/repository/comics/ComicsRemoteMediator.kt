package com.bruno13palhano.data.repository.comics

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.datasource.comics.ComicRemote
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class ComicsRemoteMediator(
    private val limit: Int = 15,
    private val mediatorComicLocalData: MediatorComicLocalData,
    private val comicRemote: ComicRemote
) : RemoteMediator<Int, Comic>() {
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (mediatorComicLocalData.getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comic>
    ): MediatorResult {
        val page: Int =
            when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastComic = getLastComic(state)
                    val nextPage = lastComic?.nextPage
                    nextPage ?: return MediatorResult.Success(endOfPaginationReached = lastComic != null)
                }
            }

        try {
            val lastOffset = getLastOffset()
            val currentOffset =
                if (loadType == LoadType.REFRESH) {
                    0
                } else {
                    lastOffset ?: 0
                }

            val response = comicRemote.getComics(currentOffset, limit)
            val endOfPaginationReached = response.isEmpty()

            mediatorComicLocalData.insertComicsAndRelatedData(
                page = page,
                nextOffset = currentOffset + limit,
                endOfPaginationReached = endOfPaginationReached,
                isRefresh = loadType == LoadType.REFRESH,
                comicNets = response
            )

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }

    private fun getLastComic(state: PagingState<Int, Comic>): Comic? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
    }

    private suspend fun getLastOffset(): Int? {
        return mediatorComicLocalData.getLastOffset()
    }
}