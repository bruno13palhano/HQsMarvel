package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.RemoteKeys
import com.bruno13palhano.data.remote.model.DataContainer
import com.bruno13palhano.data.remote.model.DataWrapper
import com.bruno13palhano.data.remote.model.Thumbnail
import com.bruno13palhano.data.remote.model.charactersummary.CharacterListNet
import com.bruno13palhano.data.remote.model.charactersummary.CharacterSummaryNet
import com.bruno13palhano.data.remote.model.comics.ComicNet
import kotlin.random.Random

private const val LENGTH = 10
private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun makeRandomComic(
    comicId: Long = getRandomLong(),
    title: String = getRandomString(),
    description: String = getRandomString(),
    thumbnail: String = getRandomString(),
    page: Int = getRandomInt(),
    isFavorite: Boolean = Random.nextBoolean()
) = Comic(
    comicId = comicId,
    title = title,
    description = description,
    thumbnail = thumbnail,
    page = page,
    isFavorite = isFavorite
)

fun makeRandomCharacter(
    id: Long = getRandomLong(),
    name: String = getRandomString(),
    description: String = getRandomString(),
    thumbnail: String = getRandomString()
) = Character(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail
)

fun makeRandomRemoteKeys(
    comicId: Long = getRandomLong(),
    prevKey: Int = getRandomInt(),
    currentPage: Int = getRandomInt(),
    nextKey: Int = getRandomInt(),
    createdAt: Long = getRandomLong()
) = RemoteKeys(
    comicId = comicId,
    prevKey = prevKey,
    currentPage = currentPage,
    nextKey = nextKey,
    createdAt = createdAt
)

fun makeRandomCharacterSummary(
    id: Long = getRandomLong(),
    comicId: Long = getRandomLong(),
    resourceURI: String = getRandomString() + "/" + getRandomInt(),
    name: String = getRandomString(),
    role: String = getRandomString()
) = CharacterSummary(
    id = id,
    comicId = comicId,
    resourceURI = resourceURI,
    name = name,
    role = role
)

fun makeRandomComicDataWrapper(
    code: Int = getRandomInt(),
    status: String = getRandomString(),
    copyright: String = getRandomString(),
    attributionText: String = getRandomString(),
    data: DataContainer<ComicNet> = makeRandomComicDataContainer(),
    etag: String = getRandomString()
) = DataWrapper(
    code = code,
    status = status,
    copyright = copyright,
    attributionText = attributionText,
    data = data,
    etag = etag
)

fun makeRandomComicDataContainer(
    offset: Int = getRandomInt(),
    limit: Int = getRandomInt(),
    total: Int = getRandomInt(),
    count: Int = getRandomInt(),
    results: List<ComicNet> =
        makeRandomComicList().map { comic ->
            ComicNet(
                id = comic.comicId,
                title = comic.title,
                description = comic.description,
                thumbnail =
                    Thumbnail(
                        comic.thumbnail?.split(".")?.first() ?: "",
                        comic.thumbnail?.split(".")?.last() ?: ""
                    ),
                characters =
                    CharacterListNet(
                        0,
                        0,
                        "",
                        makeRandomSummaryList().map { summary ->
                            CharacterSummaryNet(
                                resourceURI = summary.resourceURI,
                                name = summary.name,
                                role = summary.role
                            )
                        }
                    )
            )
        }
) = DataContainer(
    offset = offset,
    limit = limit,
    total = total,
    count = count,
    results = results
)

fun makeRandomComicList() = (1..60).map { makeRandomComic() }

fun makeRandomCharacterList() = (1..60).map { makeRandomCharacter() }

fun makeRandomSummaryList() = (1..60).map { makeRandomCharacterSummary() }

fun getRandomString() =
    (1..LENGTH)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get).joinToString("")

fun getRandomLong() = (1..LENGTH).sumOf { Random.nextLong(0, 1000) }

fun getRandomInt() = (1..LENGTH).sumOf { Random.nextInt(0, 1000) }