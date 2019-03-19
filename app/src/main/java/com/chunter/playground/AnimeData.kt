package com.chunter.playground

import com.google.gson.annotations.SerializedName

data class AnimeData(
    @SerializedName("results")
    val results: List<Anime>
)