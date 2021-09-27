package dev.ankuranurag2.trendingrepo.data.repository

import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.data.remote.GithubAPI
import dev.ankuranurag2.trendingrepo.domain.repository.RepoRepository
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: GithubAPI
) : RepoRepository {
    override suspend fun getTrendingRepositories(): List<RepoData> {
        return api.getRepositories()
    }
}