package com.github.radkoff26.nechto.ui.match

import androidx.lifecycle.LiveData
import com.github.radkoff26.nechto.data.Movie

typealias MoviesFetcher = suspend () -> List<Movie>

class MoviesPairLiveData(private val moviesFetcher: MoviesFetcher) : LiveData<MoviesPair?>() {
    private var batch: List<Movie> = emptyList()

    @Volatile
    private var loaded = false
    private var cursor: Int = -1

    init {
        value = null
    }

    suspend fun nextMovie() {
        if (!loaded) {
            fetchNewMovies()
            return
        }
        try {
            cursor++
            if (cursor == batch.size) {
                fetchNewMovies()
                return
            }
        } catch (t: Throwable) {
            postValue(null)
        }
        postPair()
    }

    private fun postPair() {
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
        batch = moviesFetcher.invoke()
        loaded = true
        cursor = 0
        if (batch.isNotEmpty()) {
            postPair()
        }
    }
}