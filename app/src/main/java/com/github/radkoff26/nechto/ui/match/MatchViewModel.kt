package com.github.radkoff26.nechto.ui.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.radkoff26.nechto.PollingWorker
import com.github.radkoff26.nechto.data.Movie
import com.github.radkoff26.nechto.exceptions.LoadingException
import com.github.radkoff26.nechto.repository.MovieRepository
import com.github.radkoff26.nechto.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private lateinit var roomId: String
    private var isLeader: Boolean = false
    private var failCallback: ((severe: Boolean) -> Unit)? = null
    private var matchCallback: ((movie: Movie) -> Unit)? = null

    private val worker = PollingWorker(this::checkForMatch)

    private val moviesCurrentBatchLiveData: MoviesPairLiveData =
        MoviesPairLiveData(this::loadMovies)
    val currentMovieLiveData: LiveData<MoviesPair?> = moviesCurrentBatchLiveData

    private val matchFragmentStateMutableLiveData: MutableLiveData<MatchFragmentState> =
        MutableLiveData(MatchFragmentState.NOT_LOADED)
    val matchFragmentStateLiveData: LiveData<MatchFragmentState> = matchFragmentStateMutableLiveData

    fun init(
        roomId: String,
        isLeader: Boolean,
        failCallback: (severe: Boolean) -> Unit,
        matchCallback: (movie: Movie) -> Unit
    ) {
        this.roomId = roomId
        this.isLeader = isLeader
        this.failCallback = failCallback
        this.matchCallback = matchCallback
    }

    fun startWatchingMatch() {
        worker.start()
    }

    fun stopWatchingMatch() {
        worker.stop()
    }

    fun nextMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            moviesCurrentBatchLiveData.nextMovie()
        }
    }

    fun dislikeMovie() {
        val movieId = currentMovieLiveData.value?.topMovie?.movieId
        if (movieId == null) {
            failCallback?.invoke(false)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieRepository.dislikeMovie(
                    roomId,
                    movieId,
                    isLeader
                )
                moviesCurrentBatchLiveData.nextMovie()
            } catch (e: LoadingException) {
                val isSevere = e.errorCode == 404
                failCallback?.invoke(isSevere)
            }
        }
    }

    fun likeMovie() {
        val movieId = currentMovieLiveData.value?.topMovie?.movieId
        if (movieId == null) {
            failCallback?.invoke(false)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieRepository.likeMovie(
                    roomId,
                    movieId,
                    isLeader
                )
                moviesCurrentBatchLiveData.nextMovie()
            } catch (e: LoadingException) {
                val isSevere = e.errorCode == 404
                failCallback?.invoke(isSevere)
            }
        }
    }

    private suspend fun loadMovies(): List<Movie> =
        withContext(viewModelScope.coroutineContext) {
            matchFragmentStateMutableLiveData.postValue(MatchFragmentState.NOT_LOADED)
            try {
                // Requesting data
                val data = movieRepository.getMovies(roomId)
                matchFragmentStateMutableLiveData.postValue(MatchFragmentState.LOADED)
                data
            } catch (t: Throwable) {
                // Possibly log
                matchFragmentStateMutableLiveData.postValue(MatchFragmentState.FAILED)
                throw RuntimeException(t)
            }
        }

    suspend fun leaveRoomAsLeaderIfLeader() = withContext(Dispatchers.IO) {
        if (isLeader) {
            try {
                roomRepository.leaveRoomAsLeader(roomId)
            } catch (e: LoadingException) {
                // Nothing to do...
            }
        }
    }

    private suspend fun checkForMatch() {
        try {
            val match = movieRepository.getMatchIfHas(roomId) ?: return
            matchCallback?.invoke(match)
        } catch (e: LoadingException) {
            // Nothing to do...
        }
    }
}