package com.bruno13palhano.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruno13palhano.data.local.data.RemoteKeysDao
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
    private lateinit var database: TestDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(
                context,
                TestDatabase::class.java
            ).build()

        remoteKeysDao = database.remoteKeysDao
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertTheRemoteKeysIntoTheDatabase() =
        runTest {
            val remoteKeys = makeRandomRemoteKeys()

            remoteKeysDao.insert(remoteKeys = remoteKeys)

            assertThat(remoteKeysDao.remoteKeyId(remoteKeys.comicId, remoteKeys.currentPage)).isEqualTo(remoteKeys)
        }

    @Test
    @Throws(Exception::class)
    fun shouldNotInsertTheRemoteKeysIntoTheDatabaseIfItAlreadyExists() =
        runTest {
            val key1 = makeRandomRemoteKeys(currentPage = 1, nextKey = 2)
            val key2 = key1.copy(currentPage = 1, nextKey = 3)
            val keys = listOf(key1, key2)

            remoteKeysDao.insertAll(keys)

            assertThat(remoteKeysDao.remoteKeyId(key2.comicId, key2.currentPage)).isNotEqualTo(key2)
        }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteRemoteKeysFromDatabase() =
        runTest {
            val keys = (1..2).map { makeRandomRemoteKeys() }

            remoteKeysDao.insertAll(keys)
            remoteKeysDao.clearRemoteKeys()

            val key1 = remoteKeysDao.remoteKeyId(keys[0].comicId, keys[0].currentPage)
            val key2 = remoteKeysDao.remoteKeyId(keys[1].comicId, keys[1].currentPage)

            assertThat(key1).isNull()
            assertThat(key2).isNull()
        }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteTheRemoteKeysWithThisIdAndPageIfExists() =
        runTest {
            val key = makeRandomRemoteKeys()

            remoteKeysDao.insert(key)
            remoteKeysDao.deleteById(key.comicId, key.currentPage)

            assertThat(remoteKeysDao.remoteKeyId(key.comicId, key.currentPage)).isNull()
        }

    @Test
    @Throws(Exception::class)
    fun shouldDoNothingWhenDeletingIfThisRemoteKeysDoesNotExist() =
        runTest {
            val key1 = makeRandomRemoteKeys()
            val key2 = makeRandomRemoteKeys()

            remoteKeysDao.insert(key1)
            remoteKeysDao.deleteById(key2.comicId, key2.currentPage)

            assertThat(remoteKeysDao.remoteKeyId(key2.comicId, key2.currentPage)).isNull()
            assertThat(remoteKeysDao.remoteKeyId(key1.comicId, key1.currentPage)).isEqualTo(key1)
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnTheLongestCreationTimeWhenThereAreRemoteKeysInDatabase() =
        runTest {
            val key1 = makeRandomRemoteKeys(createdAt = 1000)
            val key2 = makeRandomRemoteKeys(createdAt = 100)
            val key3 = makeRandomRemoteKeys(createdAt = 10)

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
            val key = makeRandomRemoteKeys()

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