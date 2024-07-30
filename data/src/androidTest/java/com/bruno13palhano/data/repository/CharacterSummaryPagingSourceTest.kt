package com.bruno13palhano.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.local.data.dao.CharacterSummaryDao
import com.bruno13palhano.data.local.data.dao.ComicsDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.mocks.MockCharacterSummaryLocalData
import com.bruno13palhano.data.mocks.makeRandomCharacterSummary
import com.bruno13palhano.data.mocks.makeRandomComic
import com.bruno13palhano.data.repository.charactersummary.CharacterSummaryPagingSource
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
internal class CharacterSummaryPagingSourceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: HQsMarvelDatabase

    private val comicId = 1L
    private val comic = makeRandomComic(comicId = comicId)
    private lateinit var comicsDao: ComicsDao
    private lateinit var characterSummaryDao: CharacterSummaryDao
    private lateinit var characterSummaryLocalData: CharacterSummaryLocalData

    @Before
    fun setup() {
        hiltRule.inject()

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
            val charactersSummary =
                (1..60)
                    .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comicId) }
                    .sortedBy { it.id }

            comicsDao.insert(comic)
            characterSummaryDao.insertAll(charactersSummary)

            val expected = charactersSummary.subList(0, 15)

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
            val charactersSummary =
                (1..120)
                    .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comicId) }
                    .sortedBy { it.id }

            comicsDao.insert(comic)
            characterSummaryDao.insertAll(charactersSummary)

            val expected = charactersSummary.subList(30, 45)

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
            val charactersSummary =
                (1..120)
                    .map { makeRandomCharacterSummary(id = it.toLong(), comicId = comicId) }
                    .sortedBy { it.id }

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