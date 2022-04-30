package com.haidev.storyapp.ui.screen.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.databinding.ItemRowStoryBinding

class StoryItemAdapter(
    private val listener: (StoryModel.Response.Story) -> Unit
) :
    ListAdapter<StoryModel.Response.Story, StoryItemAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(
        private val binding: ItemRowStoryBinding,
        private val listener: (StoryModel.Response.Story) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoryModel.Response.Story) {
            binding.item = item
            itemView.setOnClickListener {
                listener(item)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                listener: (StoryModel.Response.Story) -> Unit
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRowStoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<StoryModel.Response.Story>() {
        override fun areItemsTheSame(
            oldItem: StoryModel.Response.Story,
            newItem: StoryModel.Response.Story
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StoryModel.Response.Story,
            newItem: StoryModel.Response.Story
        ): Boolean {
            return oldItem == newItem
        }
    }
}