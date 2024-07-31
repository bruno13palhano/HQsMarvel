package com.bruno13palhano.data.database

import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.mocks.makeRandomCharacterSummary
import com.bruno13palhano.data.mocks.makeRandomComic
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
import javax.inject.Named

@HiltAndroidTest
internal class CharacterSummaryLocalDataTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: HQsMarvelDatabase
    private lateinit var comicsDao: ComicsDao
    private lateinit var characterSummaryDao: CharacterSummaryDao

    @Before
    fun setup() {
        hiltRule.inject()

        comicsDao = database.comicsDao
        characterSummaryDao = database.characterSummaryDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldInsertCharacterSummaryIntoTheDatabase() =
        runTest {
            val comic = makeRandomComic()
            val character = makeRandomCharacterSummary(comicId = comic.comicId)

            comicsDao.insert(comic)
            characterSummaryDao.insert(character)

            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = comic.comicId,
                    offset = 0,
                    limit = 1
                )
            )
                .containsExactly(character)
        }

    @Test
    fun shouldNotInsertCharacterSummaryIntoTheDatabaseIfItAlreadyExists() = runTest {
        val comic = makeRandomComic()
        val character1 = makeRandomCharacterSummary(comicId = comic.comicId)
        val character2 = makeRandomCharacterSummary(id = character1.id, comicId = comic.comicId)

        comicsDao.insert(comic)
        characterSummaryDao.insert(character1)
        characterSummaryDao.insert(character2)

        assertThat(
            characterSummaryDao.getCharacterSummaryByComicId(
                comicId = comic.comicId,
                offset = 0,
                limit = 1
            )
        )
            .doesNotContain(character2)
    }

    @Test
    fun shouldInsertAllCharacterSummaryIntoTheDatabase() =
        runTest {
            val comic = makeRandomComic()
            val characters =
                (1..10)
                    .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comic.comicId) }

            comicsDao.insert(comic)
            characterSummaryDao.insertAll(characters)

            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = comic.comicId,
                    offset = 0,
                    limit = characters.size
                )
            )
                .isEqualTo(characters)
        }

    @Test
    fun shouldReturnCharacterSummaryWithTheExpectedOffsetAndLimitWhenThereAreCharacterSummaryInDatabase() = runTest {
        val comic = makeRandomComic()
        val characters =
            (1..20)
                .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comic.comicId) }

        comicsDao.insert(comic)
        characterSummaryDao.insertAll(characters)

        assertThat(
            characterSummaryDao.getCharacterSummaryByComicId(
                comicId = comic.comicId,
                offset = 10,
                limit = 10
            )
        )
            .isEqualTo(characters.subList(10, 20))
    }

    @Test
    fun shouldReturnEmptyListWhenThereAreNoCharacterSummaryInDatabase() =
        runTest {
            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = 1,
                    offset = 0,
                    limit = 10
                ).isEmpty()
            )
        }
}