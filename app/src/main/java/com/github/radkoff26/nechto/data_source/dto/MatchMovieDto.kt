package com.github.radkoff26.nechto.data_source.dto

import com.github.radkoff26.nechto.data.Movie

data class MatchMovieDto(
    val isMatch: Boolean,
    val movie: Movie?
)
