package com.example.github.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.github.data.model.Repository
import com.example.github.databinding.ItemRepositoryBinding

class MainAdapter:
    ListAdapter<Repository, MainViewHolder>(DiffUtilCallback) {
    object DiffUtilCallback : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            oldItem.url == newItem.url

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.repository = getItem(position)
        holder.binding.executePendingBindings()
    }
}

class MainViewHolder(val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root)