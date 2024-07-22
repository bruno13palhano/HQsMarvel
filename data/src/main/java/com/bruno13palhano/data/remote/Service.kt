package com.bruno13palhano.data.remote

import com.bruno13palhano.data.remote.model.character.CharacterDataWrapper
import com.bruno13palhano.data.remote.model.comics.ComicDataWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface Service {
    @GET("comics")
    suspend fun getComics(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): ComicDataWrapper

    @GET("comics/{comicId}/characters")
    suspend fun getCharacters(
        @Path("comicId") id: Long,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDataWrapper
}