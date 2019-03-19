package com.chunter.playground

import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("title")
    val title: String
)