package com.tinude.newsheadlines.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tinude.newsheadlines.HeadlineRepository
import com.tinude.newsheadlines.network.ResultWrapper
import com.tinude.newsheadlines.network.response.Article
import com.tinude.newsheadlines.util.parseError
import com.tinude.newsheadlines.util.runIO
import kotlinx.coroutines.launch

class NewsHeadlineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HeadlineRepository(application)
    private val _uiState = mutableStateOf<NewsState>(NewsState.LOADING(true))
    val uiState: State<NewsState> = _uiState


    init {
        viewModelScope.launch {
            fetchStates()
        }
    }

    fun fetchStates() {
        try {
            runIO {
                when (val response = repository.fetchNews()) {
                    is ResultWrapper.Success -> {
                        val r = response.value
                        if (r.isEmpty() == true) {
                            _uiState.value=NewsState.Success(emptyList())
                        } else {
                            _uiState.value=NewsState.Success(r)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _uiState.value = NewsState.LOADING(false)
            parseError(e) { error ->
                _uiState.value = NewsState.ERROR(error)
            }
        }
    }
    sealed class NewsState {
        data class LOADING(val loading: Boolean = false) : NewsState()
        class Success(val data: List<Article>) : NewsState()
        class ERROR( val message: String) :  NewsState()

    }}

