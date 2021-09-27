package dev.ankuranurag2.trendingrepo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ankuranurag2.trendingrepo.data.remote.GithubAPI
import dev.ankuranurag2.trendingrepo.data.repository.RepoRepositoryImpl
import dev.ankuranurag2.trendingrepo.domain.repository.RepoRepository
import dev.ankuranurag2.trendingrepo.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGithubAPI(): GithubAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRepoRepository(api: GithubAPI): RepoRepository {
        return RepoRepositoryImpl(api)
    }
}