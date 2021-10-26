package com.huijiny.githubsearch.data

data class Repositories(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val repositories: List<Repository>
)
