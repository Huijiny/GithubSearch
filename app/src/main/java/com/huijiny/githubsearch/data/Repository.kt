package com.example.github.data.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val name: String,
    val owner: Owner,
    val private: Boolean,
    val description: String?,
    val url: String,
    val forks: Long,
    val stargazersCount: Long
)
