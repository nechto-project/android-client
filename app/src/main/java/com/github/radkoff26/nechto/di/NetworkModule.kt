package com.github.radkoff26.nechto.di

import com.github.radkoff26.nechto.Constants
import com.github.radkoff26.nechto.data_source.GenreDataSource
import com.github.radkoff26.nechto.data_source.MovieDataSource
import com.github.radkoff26.nechto.data_source.RoomDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton
    @Provides
    fun provideMovieDataSource(retrofit: Retrofit): MovieDataSource = retrofit.create()

    @Singleton
    @Provides
    fun provideRoomDataSource(retrofit: Retrofit): RoomDataSource = retrofit.create()

    @Singleton
    @Provides
    fun provideGenreDataSource(retrofit: Retrofit): GenreDataSource = retrofit.create()
}