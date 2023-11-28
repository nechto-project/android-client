package com.github.radkoff26.nechto.data_source

import com.github.radkoff26.nechto.data.Movie
import retrofit2.http.GET

interface MovieDataSource {

    @GET("/movie/search")
    suspend fun getMovies(): List<Movie>
}