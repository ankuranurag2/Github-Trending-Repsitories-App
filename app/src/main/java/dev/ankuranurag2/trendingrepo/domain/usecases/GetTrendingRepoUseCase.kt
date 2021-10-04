package dev.ankuranurag2.trendingrepo.domain.usecases

import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.domain.repository.RepoRepository
import dev.ankuranurag2.trendingrepo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrendingRepoUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(): Resource<List<RepoData>> {
        return try {
            Resource.Loading
            val list = withContext(Dispatchers.IO) { repository.getTrendingRepositories() }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}