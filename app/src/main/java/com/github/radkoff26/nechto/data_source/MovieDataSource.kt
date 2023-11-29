package com.github.radkoff26.nechto.data_source

import com.github.radkoff26.nechto.data.Movie
import com.github.radkoff26.nechto.data_source.dto.MakeDecisionDto
import com.github.radkoff26.nechto.data_source.dto.MatchMovieDto
import retrofit2.Response
import retrofit2.http.*

interface MovieDataSource {

    @GET("/movie/search/{roomId}")
    suspend fun getMovies(@Path("roomId") roomId: String): Response<List<Movie>>

    @GET("/room/ismatch/{roomId}")
    suspend fun getMatchIfHas(@Path("roomId") roomId: String): Response<MatchMovieDto>

    @POST("/room/decision/{roomId}")
    suspend fun makeDecision(
        @Path("roomId") roomId: String,
        @Query("isLeader") isLeader: Boolean,
        @Body body: MakeDecisionDto
    ): Response<Unit>
}