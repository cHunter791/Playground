package com.chunter.playground

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeService {

    @GET("search/anime")
    fun getAnime(@Query("q") query: String, @Query("page") page: Int = 1): Call<AnimeData>
}