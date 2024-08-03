package com.bruno13palhano.data.remote

import com.bruno13palhano.data.remote.model.Response
import com.bruno13palhano.data.remote.model.character.CharacterNet
import com.bruno13palhano.data.remote.model.comics.ComicNet
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
     * @return a [Response] for [ComicNet].
     */
    @GET("comics")
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ComicNet>

    /**
     * @param id The Character's id for the query.
     *
     * @return a [Response] for [CharacterNet].
     */
    @GET("characters/{id}")
    suspend fun getCharacter(
        @Path("id") id: Long
    ): Response<CharacterNet>
}