package com.bruno13palhano.data.remote

import com.bruno13palhano.data.remote.model.DataWrapper
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
     * @return a [DataWrapper] for [ComicNet].
     */
    @GET("comics")
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<ComicNet>

    /**
     * @param id The Character's id for the query.
     *
     * @return a [DataWrapper] for [CharacterNet].
     */
    @GET("characters/{id}")
    suspend fun getCharacter(
        @Path("id") id: Long
    ): DataWrapper<CharacterNet>
}