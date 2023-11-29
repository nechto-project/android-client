package com.github.radkoff26.nechto.data_source.dto

import com.google.gson.annotations.SerializedName

data class MakeDecisionDto(
    @SerializedName("idMovie")
    val movieId: Long,
    @SerializedName("answer")
    val doesLike: Boolean
)
