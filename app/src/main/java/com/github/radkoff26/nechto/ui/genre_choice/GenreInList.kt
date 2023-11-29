package com.github.radkoff26.nechto.ui.genre_choice

import com.github.radkoff26.nechto.data.Genre

data class GenreInList(
    val genre: Genre,
    val isChosen: Boolean = false
)