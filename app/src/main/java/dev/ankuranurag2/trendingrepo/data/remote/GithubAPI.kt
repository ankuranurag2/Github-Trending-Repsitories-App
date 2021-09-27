package dev.ankuranurag2.trendingrepo.data.remote

import retrofit2.http.GET

interface GithubAPI {
    @GET("/repositories")
    suspend fun getRepositories(): List<RepoDto>
}