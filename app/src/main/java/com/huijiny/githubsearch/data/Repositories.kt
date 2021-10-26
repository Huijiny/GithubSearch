package com.example.github.data.model

data class Repositories(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val repositories: List<Repository>
)
