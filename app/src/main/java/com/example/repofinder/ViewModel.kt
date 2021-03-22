package com.example.repofinder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repofinder.network.ApiService
import com.example.repofinder.network.Repository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

/**
 * View Model associated with the [MainActivity], containing info about requested [Repository].
 */
class ViewModel(apiService: ApiService? = null) : ViewModel() {

    // Store the searching result
    val repositories: LiveData<List<Repository>>
        get() = _repositories
    private val _repositories = MutableLiveData<List<Repository>>()

    private var keywordsCache = String()
    private val apiService = apiService ?: ApiService.getInstance()

    val errorMessage = MutableLiveData<String>()

    // Reset results
    fun initSearch() {
        _repositories.value = mutableListOf()
    }

    // Search repositories in IO thread and wait for the result
    fun searchRepositories(keywords: String = keywordsCache) {
        viewModelScope.launch {
            try {
                val result = apiService.searchRepositories(keywords)
                if (result.isSuccessful) {
                    _repositories.value = result.body()?.repos
                }
                keywordsCache = keywords
            } catch (httpEx: HttpException) {
                Log.v("Error: ", httpEx.toString())
            } catch (ioEx: IOException) {
                Log.v("Error: ", ioEx.toString())
            }
        }
    }
}
