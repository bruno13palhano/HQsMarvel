package com.bruno13palhano.data.repository

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruno13palhano.data.database.TestDatabase
import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.mocks.MockCharacterSummaryLocalData
import com.bruno13palhano.data.mocks.makeRandomCharacterSummary
import com.bruno13palhano.data.mocks.makeRandomComic
import com.bruno13palhano.data.repository.charactersummary.CharacterSummaryPagingSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterSummaryPagingSourceTest {
    private val comicId = 1L
    private val comic = makeRandomComic(comicId = comicId)
    private lateinit var database: TestDatabase
    private lateinit var comicsDao: ComicsDao
    private lateinit var characterSummaryDao: CharacterSummaryDao
    private lateinit var characterSummaryLocalData: CharacterSummaryLocalData

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(
                context,
                TestDatabase::class.java
            ).build()

        comicsDao = database.comicsDao
        characterSummaryDao = database.characterSummaryDao
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData() =
        runTest {
            val charactersSummary = (1..60).map { makeRandomCharacterSummary(comicId = comicId) }

            comicsDao.insert(comic)
            characterSummaryDao.insertAll(charactersSummary)

            val expected = charactersSummary.sortedBy { it.id }.subList(0, 15)

            val pagingSource =
                CharacterSummaryPagingSource(
                    characterSummaryLocalData = characterSummaryDao,
                    comicId = comicId
                )

            val pager =
                TestPager(
                    config = PagingConfig(pageSize = 15),
                    pagingSource = pagingSource
                )

            val result = pager.refresh() as LoadResult.Page

            assertThat(result.data)
                .containsExactlyElementsIn(expected)
                .inOrder()
        }

    @Test
    fun loadReturnsPageForMultipleLoadOfItemKeyedData() =
        runTest {
            val charactersSummary = (1..120).map { makeRandomCharacterSummary(comicId = comicId) }

            comicsDao.insert(comic)
            characterSummaryDao.insertAll(charactersSummary)

            val expected = charactersSummary.sortedBy { it.id }.subList(30, 45)

            val pagingSource =
                CharacterSummaryPagingSource(
                    characterSummaryLocalData = characterSummaryDao,
                    comicId = comicId
                )

            val pager =
                TestPager(
                    config = PagingConfig(pageSize = 15),
                    pagingSource = pagingSource
                )

            val page =
                with(pager) {
                    refresh()
                    append()
                    append()
                } as LoadResult.Page

            assertThat(page.data)
                .containsExactlyElementsIn(expected)
                .inOrder()
        }

    @Test
    fun refreshReturnError() =
        runTest {
            val charactersSummary = (1..120).map { makeRandomCharacterSummary(comicId = comicId) }

            // Set throwError to true to simulate an error.
            characterSummaryLocalData = MockCharacterSummaryLocalData(throwError = true)
            characterSummaryLocalData.insertAll(charactersSummary)

            val pagingSource =
                CharacterSummaryPagingSource(
                    characterSummaryLocalData = characterSummaryLocalData,
                    comicId = comicId
                )

            val pager =
                TestPager(
                    config = PagingConfig(pageSize = 15),
                    pagingSource = pagingSource
                )

            val result = pager.refresh()
            assertThat(result).isInstanceOf(LoadResult.Error::class.java)

            val page = pager.getLastLoadedPage()
            assertThat(page).isNull()
        }
}