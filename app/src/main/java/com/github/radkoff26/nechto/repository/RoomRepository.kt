package com.github.radkoff26.nechto.repository

import com.github.radkoff26.nechto.data.Genre
import com.github.radkoff26.nechto.data_source.RoomDataSource
import com.github.radkoff26.nechto.extensions.returnOrThrow
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomDataSource: RoomDataSource
) {

    suspend fun createRoomForGenres(genres: List<Genre>): String {
        val stringGenres = genres.map(Genre::title)
        return roomDataSource.createRoom(stringGenres).returnOrThrow()
    }

    suspend fun joinRoom(roomId: String): Boolean =
        roomDataSource.joinRoom(roomId).returnOrThrow()

    suspend fun hasFriendJoined(roomId: String): Boolean =
        roomDataSource.hasFriendJoined(roomId).returnOrThrow()

    suspend fun leaveRoomAsLeader(roomId: String) =
        roomDataSource.deleteRoom(roomId).returnOrThrow()
}