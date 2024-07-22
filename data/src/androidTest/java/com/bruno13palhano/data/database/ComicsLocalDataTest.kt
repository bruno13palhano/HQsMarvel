package com.bruno13palhano.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruno13palhano.data.local.data.ComicsDao
import com.bruno13palhano.data.mocks.makeRandomComic
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class ComicsLocalDataTest {
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
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldInsertTheComicIntoTheDatabase() =
        runTest {
            val comic = makeRandomComic()

            comicsDao.insert(comic = comic)

            assertThat(comicsDao.getComics()).contains(comic)
        }

    @Test
    @Throws
    fun shouldNotInsertTheComicIntoTheDatabaseIfItAlreadyExists() =
        runTest {
            val comic1 = makeRandomComic(isFavorite = false)
            val comic2 = comic1.copy(isFavorite = true)
            val comics = listOf(comic1, comic2)

            launch { comicsDao.insertAll(comics) }

            launch(Dispatchers.IO) {
                assertThat(comicsDao.getComics()).containsNoDuplicates()
                cancel()
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnEmptyListWhenThereAreNoComicsInDatabase() =
        runTest {
            launch(Dispatchers.IO) {
                assertThat(comicsDao.getComics()).isEmpty()
                cancel()
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnListOfComicsWhenThereAreComicsInDatabase() =
        runTest {
            val comics = (1..5).map { makeRandomComic() }

            comicsDao.insertAll(comics)

            launch(Dispatchers.IO) {
                assertThat(comicsDao.getComics()).containsExactlyElementsIn(comics)
                cancel()
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldDeleteComicsFromDatabaseIfComicIsNotFavorite() =
        runTest {
            val comics =
                listOf(
                    makeRandomComic(isFavorite = false),
                    makeRandomComic(isFavorite = false),
                    makeRandomComic(isFavorite = true)
                )

            comicsDao.insertAll(comics)
            comicsDao.clearComics()

            launch(Dispatchers.IO) {
                assertThat(comicsDao.getComics()).containsExactly(comics[2])
                cancel()
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnListOfFavoriteComicsWhenThereAreFavoriteComicsInDatabase() =
        runTest {
            val comics =
                listOf(
                    makeRandomComic(isFavorite = false),
                    makeRandomComic(isFavorite = true),
                    makeRandomComic(isFavorite = true)
                )

            comicsDao.insertAll(comics)

            launch(Dispatchers.IO) {
                comicsDao.getFavoriteComics().collect {
                    assertThat(it).containsExactlyElementsIn(listOf(comics[1], comics[2]))
                    cancel()
                }
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnEmptyListWhenThereAreNoFavoriteComicsInDatabase() =
        runTest {
            val comics =
                listOf(
                    makeRandomComic(isFavorite = false),
                    makeRandomComic(isFavorite = false)
                )

            comicsDao.insertAll(comics)

            launch(Dispatchers.IO) {
                comicsDao.getFavoriteComics().collect {
                    assertThat(it).isEmpty()
                    cancel()
                }
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnOnlyOneFavoriteComicWithThisIdIfExistsInDatabase() =
        runTest {
            val comics = (1..5).map { makeRandomComic(isFavorite = true) }

            comicsDao.insertAll(comics)

            launch(Dispatchers.IO) {
                comicsDao.getFavoriteComicById(comicId = comics[2].comicId).collect {
                    assertThat(it).isEqualTo(comics[2])
                    cancel()
                }
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldReturnNUllWhenThereIsNoFavoriteWithThisIdInDatabase() =
        runTest {
            val comics = (1..5).map { makeRandomComic(isFavorite = false) }

            comicsDao.insertAll(comics)

            launch(Dispatchers.IO) {
                comicsDao.getFavoriteComicById(comicId = comics[2].comicId).collect {
                    assertThat(it).isNull()
                    cancel()
                }
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldUpdateIsFavoriteAttributeIfComicExistsInDatabase() =
        runTest {
            val comic = makeRandomComic(isFavorite = false)

            comicsDao.insert(comic = comic)
            comicsDao.updateFavorite(comicId = comic.comicId, isFavorite = true)

            launch(Dispatchers.IO) {
                comicsDao.getFavoriteComicById(comicId = comic.comicId).collect {
                    assertThat(it.isFavorite).isTrue()
                    cancel()
                }
            }
        }

    @Test
    @Throws(Exception::class)
    fun shouldDoNothingWhenUpdatingIfComicDoesNotExistInDatabase() =
        runTest {
            val comic1 = makeRandomComic(comicId = 1, isFavorite = true)
            val comic2 = makeRandomComic(comicId = 2, isFavorite = false)

            comicsDao.insert(comic = comic1)
            comicsDao.updateFavorite(comicId = comic2.comicId, isFavorite = true)

            launch(Dispatchers.IO) {
                comicsDao.getFavoriteComics().collect {
                    assertThat(it).doesNotContain(comic2)
                    cancel()
                }
            }
        }
}