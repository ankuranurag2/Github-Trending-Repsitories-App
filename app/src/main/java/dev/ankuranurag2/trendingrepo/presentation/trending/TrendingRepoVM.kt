package dev.ankuranurag2.trendingrepo.presentation.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.domain.usecases.GetTrendingRepoUseCase
import dev.ankuranurag2.trendingrepo.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrendingRepoVM @Inject constructor(
    private val getTrendingRepoUseCase: GetTrendingRepoUseCase
) : ViewModel() {

    val resourceFlow = getTrendingRepoUseCase(viewModelScope)
}