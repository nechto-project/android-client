package com.github.radkoff26.nechto.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.radkoff26.nechto.data.Movie
import kotlinx.coroutines.*

class MatchViewModel : ViewModel() {
    private val moviesCurrentBatchLiveData: MoviesPairLiveData =
        MoviesPairLiveData(this::loadMovies)
    val currentMovieLiveData: LiveData<MoviesPair?> = moviesCurrentBatchLiveData

    private val matchFragmentStateMutableLiveData: MutableLiveData<MatchFragmentState> =
        MutableLiveData(MatchFragmentState.NOT_LOADED)
    val matchFragmentStateLiveData: LiveData<MatchFragmentState> = matchFragmentStateMutableLiveData

    fun nextMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            moviesCurrentBatchLiveData.nextMovie()
        }
    }

    fun likeMovie() {
        // Take id of the current movie and post it as liked
        requestLikeMovie()
        // Proceed
        nextMovie()
    }

    fun dislikeMovie() {
        // Take id of the current movie and post it as disliked
        requestDislikeMovie()
        // Proceed
        nextMovie()
    }

    private fun requestLikeMovie() {
        Log.d(
            "ViewModelTAG",
            "requestLikeMovie: liked ${moviesCurrentBatchLiveData.value?.topMovie?.name}"
        )
    }

    private fun requestDislikeMovie() {
        Log.d(
            "ViewModelTAG",
            "requestLikeMovie: disliked ${moviesCurrentBatchLiveData.value?.topMovie?.name}"
        )
    }

    private suspend fun loadMovies(): Deferred<List<Movie>> = viewModelScope.async {
        matchFragmentStateMutableLiveData.postValue(MatchFragmentState.NOT_LOADED)
        try {
            // Requesting data
            val data = requestData()
            matchFragmentStateMutableLiveData.postValue(MatchFragmentState.LOADED)
            data
        } catch (t: Throwable) {
            // Possibly log
            matchFragmentStateMutableLiveData.postValue(MatchFragmentState.FAILED)
            throw RuntimeException(t)
        }
    }

    private suspend fun requestData(): List<Movie> = withContext(Dispatchers.IO) {
        listOf(
            Movie(
                1,
                "Movie 1",
                "",
                5.0,
                "#cccccc",
                emptyList(),
                emptyList()
            ),
            Movie(
                2,
                "Movie 2",
                "",
                5.0,
                "#ff0000",
                emptyList(),
                emptyList()
            ),
            Movie(
                3,
                "Movie 3",
                "",
                5.0,
                "#00ff00",
                emptyList(),
                emptyList()
            ),
            Movie(
                4,
                "Movie 4",
                "",
                5.0,
                "#0000ff",
                emptyList(),
                emptyList()
            )
        )
    }
}