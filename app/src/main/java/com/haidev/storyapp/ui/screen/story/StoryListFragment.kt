package com.haidev.storyapp.ui.screen.story

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.FragmentStoryListBinding
import com.haidev.storyapp.ui.base.BaseFragment
import com.haidev.storyapp.util.LoadingStateAdapter
import org.koin.android.ext.android.inject

class StoryListFragment : BaseFragment<FragmentStoryListBinding, StoryListViewModel>(),
    StoryListNavigator {

    private val storyListViewModel: StoryListViewModel by inject()
    private var _binding: FragmentStoryListBinding? = null
    private val binding get() = _binding

    private lateinit var storyItemAdapter: StoryItemAdapter

    override fun onInitialization() {
        super.onInitialization()
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        initUI()
    }

    private fun initUI() {
        storyItemAdapter = StoryItemAdapter(requireContext())
        binding?.rvStory?.layoutManager = LinearLayoutManager(context)
        binding?.rvStory?.adapter = storyItemAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyItemAdapter.retry()
            }
        )
    }

    override fun setLayout(): Int = R.layout.fragment_story_list

    override fun getViewModels(): StoryListViewModel = storyListViewModel


    private fun getData() {
        storyListViewModel.responseStory.observe(this, {
            Log.d("CHECKK", it.toString())
            storyItemAdapter.submitData(lifecycle, it)
        })
    }

    override fun onObserveAction() {
        getData()
    }

    override fun onReadyAction() {

    }
}