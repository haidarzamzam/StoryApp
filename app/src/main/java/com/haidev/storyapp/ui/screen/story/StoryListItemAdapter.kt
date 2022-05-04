package com.haidev.storyapp.ui.screen.story

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.databinding.ItemRowStoryBinding

class StoryListItemAdapter(
    private val context: Context
) :
    PagingDataAdapter<StoryModel.Response.Story, StoryListItemAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(
        private val binding: ItemRowStoryBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoryModel.Response.Story) {
            binding.item = item
            itemView.setOnClickListener {
                val intent = Intent(context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_DATA_STORY, item)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivThumbnail, "thumbnail"),
                        Pair(binding.tvTitle, "title"),
                        Pair(binding.tvDesc, "desc"),
                    )
                context.startActivity(intent, optionsCompat.toBundle())
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                context: Context
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRowStoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
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