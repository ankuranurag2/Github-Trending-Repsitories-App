package dev.ankuranurag2.trendingrepo.domain.repository

import dev.ankuranurag2.trendingrepo.data.model.RepoData

interface RepoRepository {
    suspend fun getTrendingRepositories(): List<RepoData>
}