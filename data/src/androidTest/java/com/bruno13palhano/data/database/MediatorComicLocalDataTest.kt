package com.bruno13palhano.data.database

import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.data.mediator.DefaultMediatorComicLocalData
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.mocks.getRandomInt
import com.bruno13palhano.data.mocks.getRandomString
import com.bruno13palhano.data.mocks.makeRandomCharacterSummary
import com.bruno13palhano.data.mocks.makeRandomComic
import com.bruno13palhano.data.mocks.makeRandomComicDataContainer
import com.bruno13palhano.data.mocks.makeRandomComicDataWrapper
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.model.Thumbnail
import com.bruno13palhano.data.remote.model.charactersummary.CharacterListNet
import com.bruno13palhano.data.remote.model.charactersummary.CharacterSummaryNet
import com.bruno13palhano.data.remote.model.comics.ComicNet
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
internal class MediatorComicLocalDataTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: HQsMarvelDatabase
    private lateinit var mediatorComicLocalData: MediatorComicLocalData

    @Before
    fun setup() {
        hiltRule.inject()

        mediatorComicLocalData = DefaultMediatorComicLocalData(database)
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldInsertComicTheComicsIntoTheDatabase() =
        runTest {
            val offset = 0
            val limit = 10
            val comics = (1..5).map { makeRandomComic(comicId = it.toLong(), isFavorite = false) }
            val characters =
                (1..5).map { comicId ->
                    (1..limit).map {
                        val intermediateId = getRandomString() + "/" + "${comicId}${getRandomInt()}"
                        makeRandomCharacterSummary(
                            id = intermediateId.split("/").last().toLong(),
                            comicId = comicId.toLong(),
                            resourceURI = intermediateId
                        )
                    }
                }

            mediatorComicLocalData.insertComicsAndRelatedData(
                page = 0,
                nextOffset = 15,
                endOfPaginationReached = false,
                isRefresh = true,
                comicNets =
                    makeRandomComicDataWrapper(
                        data =
                            makeRandomComicDataContainer(
                                results = mapComicToComicNet(comics, characters)
                            )
                    )
            )

            val comicsResult = database.comicsDao.getComics()
            val charactersResult =
                comicsResult.map { comic ->
                    database.characterSummaryDao.getCharacterSummaryByComicId(
                        comicId = comic.id,
                        offset = offset,
                        limit = limit
                    )
                }

            assertThat(comicsResult.map { comic -> comic.id }).containsExactlyElementsIn(comics.map { comic -> comic.id })
            charactersResult.forEachIndexed { index, characterList ->
                assertThat(characters[index]).containsExactlyElementsIn(characterList)
            }
        }

    private fun mapComicToComicNet(
        comics: List<Comic>,
        characters: List<List<CharacterSummary>>
    ) = comics.map { comic ->
        ComicNet(
            id = comic.id,
            title = comic.title,
            description = comic.description,
            thumbnail =
                Thumbnail(
                    comic.thumbnail?.split(".")?.first() ?: "",
                    comic.thumbnail?.split(".")?.last() ?: ""
                ),
            characters =
                CharacterListNet(
                    items =
                        characters[comic.id.toInt() - 1].map { summary ->
                            println(summary.resourceURI)
                            CharacterSummaryNet(
                                resourceURI = summary.resourceURI,
                                name = summary.name,
                                role = summary.role
                            )
                        }
                )
        )
    }
}