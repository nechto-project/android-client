package com.github.radkoff26.nechto.data_source

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomDataSource {

    @POST("${BASE}/create")
    suspend fun createRoom(@Body stringGenres: List<String>): Response<String>

    @POST("${BASE}/join/{roomId}")
    suspend fun joinRoom(@Path("roomId") roomId: String): Response<Boolean>

    @POST("${BASE}/delete/{roomId}")
    suspend fun deleteRoom(@Path("roomId") roomId: String): Response<Unit>

    @GET("${BASE}/isjoin/{roomId}")
    suspend fun hasFriendJoined(@Path("roomId") roomId: String): Response<Boolean>

    companion object {
        private const val BASE = "/room"
    }
}