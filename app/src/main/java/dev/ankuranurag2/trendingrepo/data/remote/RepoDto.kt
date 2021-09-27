package dev.ankuranurag2.trendingrepo.data.remote

import com.google.gson.annotations.SerializedName
import dev.ankuranurag2.trendingrepo.domain.model.RepoData

data class RepoDto(
    @SerializedName("author")
    val author: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("builtBy")
    val builtBy: List<BuiltBy>,
    @SerializedName("currentPeriodStars")
    val currentPeriodStars: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("languageColor")
    val languageColor: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("url")
    val url: String
) {
    fun toRepoData() = RepoData(
        author = author,
        avatar = avatar,
        description = description,
        forks = forks,
        language = language,
        languageColor = languageColor,
        name = name,
        stars = stars
    )
}

data class BuiltBy(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("href")
    val href: String,
    @SerializedName("username")
    val username: String
)