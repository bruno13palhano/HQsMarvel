package com.bruno13palhano.data.repository.comics

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.RemoteKeys
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class ComicsRemoteMediator(
    private var offset: Int = 0,
    private val limit: Int = 15,
    private val database: HQsMarvelDatabase,
    private val comicRemoteDataSource: ComicRemoteDataSource
) : RemoteMediator<Int, Comic>() {
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (database.remoteKeysDao.getCreationTime() ?: 0) < cacheTimeout) {
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
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

        try {
            val response = comicRemoteDataSource.getComics(offset, state.config.pageSize)
            val endOfPaginationReached = response.isEmpty()

            offset += limit

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.comicsDao.getComics().map {
                        if (!it.isFavorite) {
                            database.remoteKeysDao.deleteById(comicId = it.comicId, it.page)
                        }
                    }
                    database.comicsDao.clearComics()
                }
                val prevKey = if (page > 1) page - 1 else null
                val next = if (endOfPaginationReached) null else page + 1
                val remoteKeys =
                    response.map {
                        RemoteKeys(
                            comicId = it.comicId,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = next,
                            createAt = System.currentTimeMillis()
                        )
                    }

                database.remoteKeysDao.insertAll(remoteKeys = remoteKeys)
                database.comicsDao.insertAll(comics = response.map { it.copy(page = page) })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Comic>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { comic ->
                database.remoteKeysDao.remoteKeyId(comicId = comic.comicId, page = comic.page)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Comic>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { comic ->
            database.remoteKeysDao.remoteKeyId(comicId = comic.comicId, page = comic.page)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Comic>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { comic ->
            database.remoteKeysDao.remoteKeyId(comicId = comic.comicId, page = comic.page)
        }
    }
}