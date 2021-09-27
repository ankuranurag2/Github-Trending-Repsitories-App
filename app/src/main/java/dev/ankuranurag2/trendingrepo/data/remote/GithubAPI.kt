package dev.ankuranurag2.trendingrepo.data.remote

import dev.ankuranurag2.trendingrepo.data.model.RepoData
import retrofit2.http.GET

interface GithubAPI {
    @GET("/repositories")
    suspend fun getRepositories(): List<RepoData>
}