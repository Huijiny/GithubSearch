package com.huijiny.githubsearch.network

import com.huijiny.githubsearch.data.service.SearchService

object GithubInjection {
    fun provideSearchService(): SearchService =
        GithubRetrofit.create(SearchService::class.java)
}