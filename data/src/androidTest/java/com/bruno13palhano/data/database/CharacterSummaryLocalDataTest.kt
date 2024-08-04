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
    fun shouldInsertCharacterSummaryCharacterSummaryIntoTheDatabase() =
        runTest {
            val comic = makeRandomComic()
            val character = makeRandomCharacterSummary(comicId = comic.id)

            comicsDao.insertComic(comic)
            characterSummaryDao.insertCharacterSummary(character)

            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = comic.id,
                    offset = 0,
                    limit = 1
                )
            )
                .containsExactly(character)
        }

    @Test
    fun shouldNotInsertCharacterSummaryCharacterSummaryIntoTheDatabaseIfItAlreadyExists() =
        runTest {
            val comic = makeRandomComic()
            val character1 = makeRandomCharacterSummary(comicId = comic.id)
            val character2 = makeRandomCharacterSummary(id = character1.id, comicId = comic.id)

            comicsDao.insertComic(comic)
            characterSummaryDao.insertCharacterSummary(character1)
            characterSummaryDao.insertCharacterSummary(character2)

            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = comic.id,
                    offset = 0,
                    limit = 1
                )
            )
                .doesNotContain(character2)
        }

    @Test
    fun shouldInsertCharacterSummaryAllCharacterSummaryIntoTheDatabase() =
        runTest {
            val comic = makeRandomComic()
            val characters =
                (1..10)
                    .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comic.id) }

            comicsDao.insertComic(comic)
            characterSummaryDao.insertCharactersSummary(characters)

            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = comic.id,
                    offset = 0,
                    limit = characters.size
                )
            )
                .isEqualTo(characters)
        }

    @Test
    fun shouldReturnCharacterSummaryWithTheExpectedOffsetAndLimitWhenThereAreCharacterSummaryInDatabase() =
        runTest {
            val comic = makeRandomComic()
            val characters =
                (1..20)
                    .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comic.id) }

            comicsDao.insertComic(comic)
            characterSummaryDao.insertCharactersSummary(characters)

            assertThat(
                characterSummaryDao.getCharacterSummaryByComicId(
                    comicId = comic.id,
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