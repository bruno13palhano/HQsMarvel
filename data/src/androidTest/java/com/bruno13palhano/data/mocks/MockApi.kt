package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.Service
import com.bruno13palhano.data.remote.model.Thumbnail
import com.bruno13palhano.data.remote.model.character.CharacterDataContainer
import com.bruno13palhano.data.remote.model.character.CharacterDataWrapper
import com.bruno13palhano.data.remote.model.character.CharacterNet
import com.bruno13palhano.data.remote.model.charactersummary.CharacterListNet
import com.bruno13palhano.data.remote.model.charactersummary.CharacterSummaryNet
import com.bruno13palhano.data.remote.model.comics.ComicDataWrapper
import com.bruno13palhano.data.remote.model.comics.ComicNet
import okio.IOException

class MockApi(
    private val comics: List<Comic> = makeRandomComicList(),
    private val characters: List<Character> = makeRandomCharacterList()
) : Service {
    override suspend fun getComics(
        offset: Int,
        limit: Int
    ): ComicDataWrapper {
        // Simulate a exception
        if (limit == -1) throw IOException()

        val response =
            try {
                comics.subList(offset, limit)
            } catch (e: Exception) {
                emptyList()
            }

        return makeRandomComicDataWrapper(
            data =
                makeRandomComicDataContainer(
                    offset = offset,
                    limit = limit,
                    total = comics.size,
                    count = response.size,
                    results =
                        response.map { comic ->
                            ComicNet(
                                id = comic.comicId,
                                title = comic.title,
                                description = comic.description,
                                thumbnail = Thumbnail("", ""),
                                characters =
                                    CharacterListNet(
                                        available = getRandomInt(),
                                        returned = getRandomInt(),
                                        collectionURI = getRandomString(),
                                        items =
                                            makeRandomSummaryList().map { character ->
                                                CharacterSummaryNet(
                                                    resourceURI = character.resourceURI,
                                                    name = character.name,
                                                    role = character.role
                                                )
                                            }
                                    )
                            )
                        }
                )
        )
    }

    override suspend fun getCharacter(id: Long): CharacterDataWrapper {
        val response = characters.filter { it.id == id }

        return CharacterDataWrapper(
            code = 200,
            status = "Ok",
            copyright = "",
            attributionText = "",
            etag = "",
            data =
                CharacterDataContainer(
                    offset = 0,
                    limit = 10,
                    total = 1,
                    count = 1,
                    results =
                        response.map {
                            CharacterNet(
                                id = it.id,
                                name = it.name,
                                description = it.description,
                                thumbnail = Thumbnail("", "")
                            )
                        }
                )
        )
    }
}