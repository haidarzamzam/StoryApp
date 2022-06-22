package com.haidev.storyapp.ui.screen.story

import android.os.Bundle
import com.bumptech.glide.Glide
import com.haidev.storyapp.R
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.databinding.ActivityDetailStoryBinding
import com.haidev.storyapp.ui.base.BaseActivity
import org.koin.android.ext.android.inject

class DetailStoryActivity : BaseActivity<ActivityDetailStoryBinding, DetailStoryViewModel>(),
    DetailStoryNavigator {

    private val detailStoryViewModel: DetailStoryViewModel by inject()
    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding

    private lateinit var dataStory: StoryModel.Response.Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        detailStoryViewModel.navigator = this
        initUI()
    }

    private fun initUI() {
        dataStory = intent.getParcelableExtra<StoryModel.Response.Story>(EXTRA_DATA_STORY)
                as StoryModel.Response.Story

        binding?.ivBack?.setOnClickListener {
            finish()
        }

        binding?.ivThumbnail?.let { Glide.with(this).load(dataStory.photoUrl).into(it) }
        binding?.tvTitle?.text = dataStory.name
        binding?.tvDesc?.text = dataStory.description
    }

    companion object {
        const val EXTRA_DATA_STORY = "EXTRA_DATA_STORY"
    }

    override fun setLayout(): Int = R.layout.activity_detail_story

    override fun getViewModels(): DetailStoryViewModel = detailStoryViewModel
}