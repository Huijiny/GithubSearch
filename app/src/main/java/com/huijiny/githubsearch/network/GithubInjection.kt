package com.example.github.network

import com.example.github.data.service.SearchService
import retrofit2.Retrofit

object GithubInjection {
    fun provideSearchService(): SearchService =
        GithubRetrofit.create(SearchService::class.java)
}