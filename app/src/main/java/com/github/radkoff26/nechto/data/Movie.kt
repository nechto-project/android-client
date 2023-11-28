package com.github.radkoff26.nechto.data

data class Movie(
    val movieId: Int,
    val name: String,
    val description: String,
    val score: Double,
    val poster: String?,
    val genres: List<Genre>,
    val directors: List<Director>
)
