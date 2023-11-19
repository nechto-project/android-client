package com.github.radkoff26.nechto.ui.match

import com.github.radkoff26.nechto.data.Movie

data class MoviesPair(
    val topMovie: Movie,
    val bottomMovie: Movie?
)