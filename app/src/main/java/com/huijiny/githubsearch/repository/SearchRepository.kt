package com.huijiny.githubsearch.repository

import com.huijiny.githubsearch.data.model.Repositories
import com.huijiny.githubsearch.data.model.toEntity
import com.huijiny.githubsearch.data.service.SearchService
import io.reactivex.Single

class SearchRepository(
    private val searchService: SearchService
) {
    fun searchRepository(query: String): Single<Repositories> {
        return searchService.searchRepositories(query)
            .map { response ->
                response.toEntity()
            }
    }
}