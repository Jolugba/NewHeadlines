package com.tinude.newsheadlines.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tinude.newsheadlines.HeadlineRepository
import com.tinude.newsheadlines.network.ResultWrapper
import com.tinude.newsheadlines.network.response.Article
import com.tinude.newsheadlines.util.parseError
import com.tinude.newsheadlines.util.runIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsHeadlineViewModel @Inject constructor(
    private val repository: HeadlineRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<NewsState>(NewsState.LOADING(true))
    val uiState: State<NewsState> = _uiState


    init {
      runIO {
            fetchNews()
        }
    }

    fun fetchNews() {
        try {
            runIO {
                when (val response = repository.fetchNews()) {
                    is ResultWrapper.Success -> {
                        val r = response.value
                        if (r.isEmpty()) {
                            _uiState.value = NewsState.Success(emptyList())
                        } else {
                            _uiState.value = NewsState.Success(r)
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
        class ERROR(val message: String) : NewsState()

    }
}

