package com.bruno13palhano.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.data.dao.RemoteKeysDao
import com.bruno13palhano.data.mocks.makeRandomComic
import com.bruno13palhano.data.mocks.makeRandomRemoteKeys
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RemoteKeysLocalDataTest {
    private lateinit var remoteKeysDao: RemoteKeysDao
    private lateinit var comicsDao: ComicsDao
    private lateinit var database: TestDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(
                context,
                TestDatabase::class.java
            ).build()

        comicsDao = database.comicsDao
        remoteKeysDao = database.remoteKeysDao
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.clearAllTables()
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertTheRemoteKeysIntoTheDatabase() =
        runTest {
            val comic = makeRandomComic()
            val remoteKeys = makeRandomRemoteKeys(comicId = comic.comicId)

            comicsDao.insert(comic)
            remoteKeysDao.insert(remoteKeys = remoteKeys)

            assertThat(remoteKeysDao.getRemoteKeyByComicId(remoteKeys.comicId)).isEqualTo(remoteKeys)
        }

    @Test
    @Throws(Exception::class)
    fun shouldNotInsertTheRemoteKeysIntoTheDatabaseIfItAlreadyExists() =
        runTest {
            val comic = makeRandomComic()
            val key1 = makeRandomRemoteKeys(comicId = comic.comicId, currentPage = 1, nextKey = 2)
            val key2 = key1.copy(currentPage = 1, nextKey = 3)
            val keys = listOf(key1, key2)

            comicsDao.insert(comic)
            remoteKeysDao.insertAll(keys)

            assertThat(remoteKeysDao.getRemoteKeyByComicId(key2.comicId)).isNotEqualTo(key2)
        }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteRemoteKeysFromDatabase() =
        runTest {
            val comic1 = makeRandomComic()
            val comic2 = makeRandomComic()

            val keys = listOf(
                makeRandomRemoteKeys(comicId = comic1.comicId, currentPage = 1, nextKey = 2),
                makeRandomRemoteKeys(comicId = comic2.comicId, currentPage = 2, nextKey = 3)
            )

            comicsDao.insert(comic1)
            comicsDao.insert(comic2)
            remoteKeysDao.insertAll(keys)
            remoteKeysDao.clearRemoteKeys()

            val key1 = remoteKeysDao.getRemoteKeyByComicId(keys[0].comicId)
            val key2 = remoteKeysDao.getRemoteKeyByComicId(keys[1].comicId)

            assertThat(key1).isNull()
            assertThat(key2).isNull()
        }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteTheRemoteKeysWithThisIdAndPageIfExists() =
        runTest {
            val comic = makeRandomComic()
            val key = makeRandomRemoteKeys(comicId = comic.comicId)

            comicsDao.insert(comic)
            remoteKeysDao.insert(key)
            remoteKeysDao.deleteById(key.comicId)

            assertThat(remoteKeysDao.getRemoteKeyByComicId(key.comicId)).isNull()
        }

    @Test
    @Throws(Exception::class)
    fun shouldDoNothingWhenDeletingIfThisRemoteKeysDoesNotExist() =
        runTest {
            val comic1 = makeRandomComic()
            val comic2 = makeRandomComic()
            val key1 = makeRandomRemoteKeys(comicId = comic1.comicId)
            val key2 = makeRandomRemoteKeys(comicId = comic2.comicId)

            comicsDao.insert(comic1)
            remoteKeysDao.insert(key1)
            remoteKeysDao.deleteById(key2.comicId)

            assertThat(remoteKeysDao.getRemoteKeyByComicId(key2.comicId)).isNull()
            assertThat(remoteKeysDao.getRemoteKeyByComicId(key1.comicId)).isEqualTo(key1)
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnTheLongestCreationTimeWhenThereAreRemoteKeysInDatabase() =
        runTest {
            val comics = (1..3).map { makeRandomComic() }
            val key1 = makeRandomRemoteKeys(comicId = comics[0].comicId, createdAt = 1000)
            val key2 = makeRandomRemoteKeys(comicId = comics[1].comicId, createdAt = 100)
            val key3 = makeRandomRemoteKeys(comicId = comics[2].comicId, createdAt = 10)

            comicsDao.insertAll(comics)
            remoteKeysDao.insertAll(listOf(key1, key2, key3))

            assertThat(remoteKeysDao.getCreationTime()).isEqualTo(key1.createdAt)
        }

    @Test
    @Throws(Exception::class)
    fun getCreationTimeShouldReturnNullIfThereAreNoRemoteKeysInDatabase() =
        runTest {
            assertThat(remoteKeysDao.getCreationTime()).isNull()
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnTheCurrentPageIfThereAreRemoteKeysInDatabase() =
        runTest {
            val comic = makeRandomComic()
            val key = makeRandomRemoteKeys(comicId = comic.comicId)

            comicsDao.insert(comic)
            remoteKeysDao.insert(key)

            assertThat(remoteKeysDao.getCurrentPage()).isEqualTo(key.currentPage)
        }

    @Test
    @Throws(Exception::class)
    fun getCurrentPageShouldReturnNullWhenThereAreNoRemoteKeysInDatabase() =
        runTest {
            assertThat(remoteKeysDao.getCurrentPage()).isNull()
        }
}