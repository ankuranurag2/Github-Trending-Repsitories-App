package dev.ankuranurag2.trendingrepo.domain.usecases

import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.domain.repository.RepoRepository
import dev.ankuranurag2.trendingrepo.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTrendingRepoUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke(): Flow<Resource<List<RepoData>>> = flow {
        try {
            emit(Resource.Loading)
            val list = repository.getTrendingRepositories()
            emit(Resource.Success(list))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}