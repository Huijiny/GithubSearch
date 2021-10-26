package com.huijiny.githubsearch.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huijiny.githubsearch.data.model.Repository

object RecyclerBindingAdapter {
    @JvmStatic
    @BindingAdapter("app:submitList")
    fun submitList(recyclerView: RecyclerView, repositories: List<Repository>?) {
        (recyclerView.adapter as MainAdapter).submitList(repositories)
    }
}