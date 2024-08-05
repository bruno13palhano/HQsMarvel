package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.Service
import com.bruno13palhano.data.remote.model.DataContainer
import com.bruno13palhano.data.remote.model.Response
import com.bruno13palhano.data.remote.model.Thumbnail
import com.bruno13palhano.data.remote.model.character.CharacterNet
import com.bruno13palhano.data.remote.model.charactersummary.CharacterListNet
import com.bruno13palhano.data.remote.model.charactersummary.CharacterSummaryNet
import com.bruno13palhano.data.remote.model.comics.ComicNet
import okio.IOException

class MockApi(
    private val comics: List<Comic> = makeRandomComicList(),
    private val characters: List<Character> = makeRandomCharacterList()
) : Service {
    override suspend fun getComics(
        offset: Int,
        limit: Int
    ): Response<ComicNet> {
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
                    results =
                        response.map { comic ->
                            ComicNet(
                                id = comic.id,
                                title = comic.title,
                                description = comic.description,
                                thumbnail = Thumbnail("", ""),
                                characters =
                                    CharacterListNet(
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

    override suspend fun getCharacter(id: Long): Response<CharacterNet> {
        val response = characters.filter { it.id == id }

        return Response(
            copyright = "",
            attributionText = "",
            data =
                DataContainer(
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