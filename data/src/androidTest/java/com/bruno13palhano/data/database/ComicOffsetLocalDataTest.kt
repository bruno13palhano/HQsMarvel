package com.bruno13palhano.data.database

import com.bruno13palhano.data.local.data.dao.ComicOffsetDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.ComicOffset
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
internal class ComicOffsetLocalDataTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: HQsMarvelDatabase
    private lateinit var comicOffsetDao: ComicOffsetDao

    @Before
    fun setup() {
        hiltRule.inject()

        comicOffsetDao = database.comicOffsetDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldInsertComicOffsetComicOffsetIntoTheDatabase() =
        runTest {
            val comicOffset = ComicOffset(id = 1, lastOffset = 10)

            comicOffsetDao.insertComicOffset(comicOffset)

            assertThat(comicOffsetDao.getLastOffset()).isEqualTo(comicOffset.lastOffset)
        }

    @Test
    fun shouldReplaceComicOffsetIfItAlreadyExists() =
        runTest {
            val comicOffset1 = ComicOffset(id = 1, lastOffset = 10)
            val comicOffset2 = ComicOffset(id = 1, lastOffset = 20)

            comicOffsetDao.insertComicOffset(comicOffset1)
            comicOffsetDao.insertComicOffset(comicOffset2)

            assertThat(comicOffsetDao.getLastOffset()).isEqualTo(comicOffset2.lastOffset)
        }

    @Test
    fun shouldReturnLastOffsetWhenThereAreComicOffsetInDatabase() =
        runTest {
            val comicOffset1 = ComicOffset(id = 1, lastOffset = 10)
            val comicOffset2 = ComicOffset(id = 2, lastOffset = 20)
            val comicOffset3 = ComicOffset(id = 3, lastOffset = 30)

            comicOffsetDao.insertComicOffset(comicOffset1)
            comicOffsetDao.insertComicOffset(comicOffset2)
            comicOffsetDao.insertComicOffset(comicOffset3)

            assertThat(comicOffsetDao.getLastOffset()).isEqualTo(comicOffset3.lastOffset)
        }

    @Test
    fun shouldReturnNullWhenThereAreNoComicOffsetInDatabase() =
        runTest {
            assertThat(comicOffsetDao.getLastOffset()).isNull()
        }
}