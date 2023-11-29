package com.github.radkoff26.nechto.ui.genre_choice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.radkoff26.nechto.LoadableData
import com.github.radkoff26.nechto.data.Genre
import com.github.radkoff26.nechto.exceptions.LoadingException
import com.github.radkoff26.nechto.repository.GenreRepository
import com.github.radkoff26.nechto.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GenreChoiceViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val genresDataMutableLiveData: MutableLiveData<LoadableData<List<Genre>>> =
        MutableLiveData(LoadableData.Loading())
    val genresDataLiveData: LiveData<LoadableData<List<Genre>>> = genresDataMutableLiveData

    fun loadGenresIfNotYet() {
        if (genresDataMutableLiveData.value !is LoadableData.Loaded) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val genres = genreRepository.getGenres()
                    genresDataMutableLiveData.postValue(
                        LoadableData.Loaded(genres)
                    )
                } catch (e: LoadingException) {
                    genresDataMutableLiveData.postValue(
                        LoadableData.Failed(e.errorCode, e.message)
                    )
                }
            }
        }
    }

    suspend fun createRoom(genres: List<Genre>): String? = withContext(Dispatchers.IO) {
        try {
            roomRepository.createRoomForGenres(genres)
        } catch (e: LoadingException) {
            Log.e(TAG, "createRoom: error while creating room!", e)
            null
        }
    }

    companion object {
        const val TAG = "GenreChoiceViewModel_TAG"
    }
}