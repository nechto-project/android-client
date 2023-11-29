package com.github.radkoff26.nechto.repository

import com.github.radkoff26.nechto.data.Movie
import com.github.radkoff26.nechto.data_source.MovieDataSource
import com.github.radkoff26.nechto.data_source.dto.MakeDecisionDto
import com.github.radkoff26.nechto.extensions.returnOrThrow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val dataSource: MovieDataSource
) {

    suspend fun getMovies(roomId: String): List<Movie> =
        dataSource.getMovies(roomId).returnOrThrow()

    suspend fun likeMovie(roomId: String, movieId: Long, isLeader: Boolean) =
        dataSource.makeDecision(roomId, isLeader, MakeDecisionDto(movieId, true)).returnOrThrow()

    suspend fun dislikeMovie(roomId: String, movieId: Long, isLeader: Boolean) =
        dataSource.makeDecision(roomId, isLeader, MakeDecisionDto(movieId, false)).returnOrThrow()

    suspend fun getMatchIfHas(roomId: String): Movie? =
        dataSource.getMatchIfHas(roomId).returnOrThrow().movie
}