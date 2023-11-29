package com.github.radkoff26.nechto.repository

import com.github.radkoff26.nechto.data.Genre
import com.github.radkoff26.nechto.data_source.GenreDataSource
import com.github.radkoff26.nechto.exceptions.LoadingException
import com.github.radkoff26.nechto.extensions.returnOrThrow
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val genreDataSource: GenreDataSource
) {

    @Throws(LoadingException::class)
    suspend fun getGenres(): List<Genre> = genreDataSource.getAllGenres().returnOrThrow()
}