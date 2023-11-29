package com.github.radkoff26.nechto.data

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genreId")
    val id: Int,
    @SerializedName("name")
    val title: String
)