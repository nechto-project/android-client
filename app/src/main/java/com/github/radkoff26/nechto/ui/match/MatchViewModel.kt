package com.github.radkoff26.nechto.ui.match

import android.util.Log
import androidx.lifecycle.*
import com.github.radkoff26.nechto.data.Movie
import com.github.radkoff26.nechto.data_source.MovieDataSource
import kotlinx.coroutines.*

class MatchViewModel(private val movieDataSource: MovieDataSource) : ViewModel() {
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
            val data = movieDataSource.getMovies()
            matchFragmentStateMutableLiveData.postValue(MatchFragmentState.LOADED)
            data
        } catch (t: Throwable) {
            // Possibly log
            matchFragmentStateMutableLiveData.postValue(MatchFragmentState.FAILED)
            throw RuntimeException(t)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val dataSource: MovieDataSource) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MatchViewModel(dataSource) as T
        }
    }
}