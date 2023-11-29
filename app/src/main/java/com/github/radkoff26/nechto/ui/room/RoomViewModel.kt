package com.github.radkoff26.nechto.ui.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.radkoff26.nechto.PollingWorker
import com.github.radkoff26.nechto.exceptions.LoadingException
import com.github.radkoff26.nechto.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    private lateinit var roomId: String

    private val worker = PollingWorker(this::checkIfFriendIsPresent)

    private val participantsCountMutableLiveData: MutableLiveData<Int> = MutableLiveData(1)
    val participantsCountLiveData: LiveData<Int> = participantsCountMutableLiveData

    fun init(roomId: String) {
        this.roomId = roomId
    }

    fun startPollingForCount() {
        worker.start()
    }

    fun stopPollingForCount() {
        worker.stop()
    }

    private suspend fun checkIfFriendIsPresent() {
        try {
            val hasJoined = roomRepository.hasFriendJoined(roomId)
            if (hasJoined && participantsCountMutableLiveData.value == 1) {
                participantsCountMutableLiveData.postValue(2)
            } else if (!hasJoined && participantsCountMutableLiveData.value == 2) {
                participantsCountMutableLiveData.postValue(1)
            }
        } catch (e: LoadingException) {
            // Nothing to do...
        }
    }
}