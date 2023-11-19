package com.github.radkoff26.nechto.ui.match

import androidx.lifecycle.LiveData
import com.github.radkoff26.nechto.data.Movie
import kotlinx.coroutines.Deferred

typealias MoviesFetcher = suspend () -> Deferred<List<Movie>>

class MoviesPairLiveData(private val moviesFetcher: MoviesFetcher) : LiveData<MoviesPair?>() {
    private var batch: List<Movie> = emptyList()
    private var cursor: Int = -1

    init {
        value = null
    }

    suspend fun nextMovie() {
        try {
            cursor++
            if (cursor == batch.size) {
                fetchNewMovies()
            }
        } catch (t: Throwable) {
            postValue(null)
        }
        val pair = MoviesPair(
            batch[cursor],
            if (cursor + 1 == batch.size) {
                null
            } else {
                batch[cursor + 1]
            }
        )
        postValue(pair)
    }

    private suspend fun fetchNewMovies() {
        batch = moviesFetcher.invoke().await()
        cursor = 0
    }
}