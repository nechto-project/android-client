package com.github.radkoff26.nechto.data_source

import com.github.radkoff26.nechto.data.Genre
import retrofit2.Response
import retrofit2.http.GET

interface GenreDataSource {

    @GET("/genre")
    suspend fun getAllGenres(): Response<List<Genre>>
}