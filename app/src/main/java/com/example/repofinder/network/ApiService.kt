package com.example.repofinder.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com/search/"

/**
 * Uses the Retrofit builder to create a retrofit object.
 */
class ApiService {
    companion object {
        private var INSTANCE: ApiService? = null
        fun getInstance(): ApiService =
            INSTANCE ?: ApiService().also { INSTANCE = it }
    }

    private val httpClient = lazy { OkHttpClient() }
    private val retrofit = lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.value)
            .build()
    }

    suspend fun searchRepositories(keywords: String) = retrofit.value.create(GitHubApi::class.java)
        .searchRepositories(keywords)
}

interface GitHubApi {
    @GET("repositories")
    suspend fun searchRepositories(
        @Query("q") keywords: String
    ): Response<SearchResult>
}



