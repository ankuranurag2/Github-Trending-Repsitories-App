package dev.ankuranurag2.trendingrepo.presentation.trending

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ankuranurag2.trendingrepo.R
import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.domain.usecases.GetTrendingRepoUseCase
import dev.ankuranurag2.trendingrepo.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingRepoVM @Inject constructor(
    private val getTrendingRepoUseCase: GetTrendingRepoUseCase
) : ViewModel() {

    // UI state exposed to the Composable(UI)
    private val _uiState = MutableStateFlow(RepoListUiState(loading = true))
    val uiState: StateFlow<RepoListUiState> = _uiState.asStateFlow()

    //Fetch data on initial load
    init {
        refreshPosts()
    }

    fun refreshPosts() {
        viewModelScope.launch {
            val result = getTrendingRepoUseCase()
            _uiState.update {
                when (result) {
                    is Resource.Loading -> it.copy(loading = true)
                    is Resource.Success -> it.copy(repoList = result.data, errorMsgId = null, loading = false)
                    is Resource.Error -> {
                        val errorMsgId = R.string.loading_error
                        it.copy(errorMsgId = errorMsgId, loading = false)
                    }
                }
            }
        }
    }

    fun sortData(sortBy: SortBy){
        _uiState.update {state->
            when (sortBy) {
                SortBy.NAME -> state.copy(repoList = state.repoList.sortedBy { it.name })
                SortBy.STAR -> state.copy(repoList = state.repoList.sortedByDescending { it.stars })
            }
        }
    }
}

/**
 * UI state for the Trending Repo List screen
 */
data class RepoListUiState(
    val repoList: List<RepoData> = emptyList(),
    val loading: Boolean = false,
    @StringRes val errorMsgId: Int? = null
) {
    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = repoList.isEmpty() && errorMsgId == null && loading
}

/**
 * Enum for sorting types
 */
enum class SortBy{
    NAME,
    STAR
}