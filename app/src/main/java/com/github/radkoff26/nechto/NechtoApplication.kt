package com.github.radkoff26.nechto

import android.app.Application
import com.github.radkoff26.nechto.data_source.MovieDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NechtoApplication : Application() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val movieDataSource: MovieDataSource by lazy {
        retrofit.create()
    }

    companion object {
        private const val BASE_URL = "http://10.5.5.114:8081"
    }
}