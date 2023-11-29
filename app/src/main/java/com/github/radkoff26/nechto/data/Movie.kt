package com.github.radkoff26.nechto.data

data class Movie(
    val movieId: Long,
    val name: String,
    val description: String,
    val score: Double,
    val poster: String?,
    val genres: List<String>
)
