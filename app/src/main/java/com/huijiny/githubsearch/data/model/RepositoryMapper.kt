package com.huijiny.githubsearch.data.model

import com.huijiny.githubsearch.data.model.response.OwnerResponse
import com.huijiny.githubsearch.data.model.response.RepositoriesResponse

fun RepositoriesResponse.toEntity(): Repositories {
    return Repositories(
        totalCount = this.totalCount,
        incompleteResults = this.incompleteResults,
        repositories = this.repositories.map {
            Repository(
                name = it.name,
                url = it.url,
                description = it.description,
                private = it.private,
                owner = it.owner.toEntity(),
                forks = it.forks,
                stargazersCount = it.stargazersCount
            )
        }
    )
}

fun OwnerResponse.toEntity(): Owner {
    return Owner(
        login = this.login,
        avatarUrl = this.avatarUrl
    )
}
