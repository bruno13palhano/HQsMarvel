package com.bruno13palhano.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruno13palhano.data.database.TestDatabase
import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicOffsetDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.data.dao.RemoteKeysDao
import com.bruno13palhano.data.mocks.MockApi
import com.bruno13palhano.data.mocks.MockMediatorComicLocalData
import com.bruno13palhano.data.mocks.makeRandomComic
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.Service
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import com.bruno13palhano.data.remote.datasource.comics.DefaultComicRemoteDataSource
import com.bruno13palhano.data.repository.comics.ComicsRemoteMediator
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
class ComicRemoteMediatorTest {
    private val comics = (1..120).map { makeRandomComic() }
    private lateinit var api: Service
    private lateinit var remoteDataSource: ComicRemoteDataSource
    private lateinit var database: TestDatabase
    private lateinit var comicsDao: ComicsDao
    private lateinit var remoteKeysDao: RemoteKeysDao
    private lateinit var comicOffsetDao: ComicOffsetDao
    private lateinit var characterSummaryDao: CharacterSummaryDao

    private lateinit var mediatorDataSource: MockMediatorComicLocalData

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(
                context,
                TestDatabase::class.java
            ).build()

        comicsDao = database.comicsDao
        remoteKeysDao = database.remoteKeysDao
        comicOffsetDao = database.comicOffsetDao
        characterSummaryDao = database.characterSummaryDao
        api = MockApi(comics = comics)
        remoteDataSource = DefaultComicRemoteDataSource(service = api)

        mediatorDataSource =
            MockMediatorComicLocalData(
                comicLocalData = comicsDao,
                remoteKeysLocalData = remoteKeysDao,
                comicOffsetLocalData = comicOffsetDao,
                characterSummaryLocalData = characterSummaryDao
            )
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        api = MockApi(comics = emptyList())
        remoteDataSource = DefaultComicRemoteDataSource(service = api)
        database.close()
    }

    @Test
    fun refreshLoadReturnSuccessResultWhenMoreDataIsPresent() =
        runTest {
            val remoteMediator =
                ComicsRemoteMediator(
                    limit = 15,
                    mediatorComicLocalData = mediatorDataSource,
                    comicRemoteDataSource = remoteDataSource
                )
            val pagingState =
                PagingState<Int, Comic>(
                    listOf(),
                    null,
                    PagingConfig(15),
                    15
                )

            val result = remoteMediator.load(LoadType.REFRESH, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() =
        runTest {
            api = MockApi(comics = emptyList())
            remoteDataSource = DefaultComicRemoteDataSource(service = api)

            val remoteMediator =
                ComicsRemoteMediator(
                    limit = 15,
                    mediatorComicLocalData = mediatorDataSource,
                    comicRemoteDataSource = remoteDataSource
                )
            val pagingState =
                PagingState<Int, Comic>(
                    listOf(),
                    null,
                    PagingConfig(15),
                    15
                )

            val result = remoteMediator.load(LoadType.REFRESH, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() =
        runTest {
            // Set limit to -1 to simulate an error.
            val remoteMediator =
                ComicsRemoteMediator(
                    limit = -1,
                    mediatorComicLocalData = mediatorDataSource,
                    comicRemoteDataSource = remoteDataSource
                )
            val pagingState =
                PagingState<Int, Comic>(
                    listOf(),
                    null,
                    PagingConfig(15),
                    15
                )

            val result = remoteMediator.load(LoadType.REFRESH, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Error)
        }
}