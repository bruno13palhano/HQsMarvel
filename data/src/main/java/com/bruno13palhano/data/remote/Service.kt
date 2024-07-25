package com.bruno13palhano.data.remote

import com.bruno13palhano.data.remote.model.character.CharacterDataWrapper
import com.bruno13palhano.data.remote.model.comics.ComicDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface for fetching data from API.
 */
internal interface Service {
    /**
     * @param offset The offset for the query.
     *
     * @param limit The limit for the query. Must be greater than 0
     * and equal to or less than 100.
     *
     * @return a [ComicDataWrapper].
     */
    @GET("comics")
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): ComicDataWrapper

    /**
     * @param id The Character's id for the query.
     *
     * @return a [CharacterDataWrapper].
     */
    @GET("characters/{id}")
    suspend fun getCharacter(
        @Path("id") id: Long
    ): CharacterDataWrapper
}