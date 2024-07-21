package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.RemoteKeys
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

private fun getRandomString() =
    (1..LENGTH)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get).joinToString("")

private fun getRandomLong() = (1..LENGTH).sumOf { Random.nextLong(0, 1000) }

private fun getRandomInt() = (1..LENGTH).sumOf { Random.nextInt(0, 1000) }

private fun getRandomFloat() = (1..LENGTH).map { Random.nextFloat() }.sum()

private fun getRandomBoolean() = Random.nextBoolean()