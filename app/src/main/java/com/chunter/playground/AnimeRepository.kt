package com.chunter.playground

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimeRepository {

    suspend fun searchAnime(query: String): List<String> = coroutineScope {
        val animeService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.jikan.moe/v3/")
            .build()
            .create(AnimeService::class.java)

        val result = withContext(Dispatchers.Default) { animeService.getAnime(query).execute() }

        return@coroutineScope if (result.isSuccessful) {
            result.body()?.results?.map { anime -> anime.title } ?: emptyList()
        } else {
            emptyList()
        }
    }
}