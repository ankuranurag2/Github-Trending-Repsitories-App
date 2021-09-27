package dev.ankuranurag2.trendingrepo.domain.repository

import dev.ankuranurag2.trendingrepo.data.remote.RepoDto

interface RepoRepository {
    suspend fun getTrendingRepositories(): List<RepoDto>
}