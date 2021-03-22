package com.example.repofinder.network

import com.google.gson.annotations.SerializedName

/**
 * Gets repositories's information from the [ApiService] and updates the [LiveData].
 */
data class Owner(
        @SerializedName("avatar_url")
        val avatarUrl: String
)

data class Repository(
        @SerializedName("full_name")
        val name: String,
        val owner: Owner,
        val description: String,
        @SerializedName("html_url")
        val url: String,
        @SerializedName("open_issues_count")
        val issues: Int,
        @SerializedName("stargazers_count")
        val stars: Int,
        @SerializedName("forks_count")
        val forks: Int,
        val language: String?
) {
    fun isLanguageValid() = language?.isNotEmpty() ?: false
}

data class SearchResult(
        @SerializedName("items")
        val repos: List<Repository>
)