package com.haidev.storyapp.ui.screen.story

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.FragmentStoryListBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseFragment
import com.haidev.storyapp.ui.screen.login.LoginActivity
import com.haidev.storyapp.util.LoadingStateAdapter
import org.koin.android.ext.android.inject

class StoryListFragment : BaseFragment<FragmentStoryListBinding, StoryListViewModel>(),
    StoryListNavigator {

    private val storyListViewModel: StoryListViewModel by inject()
    private var _binding: FragmentStoryListBinding? = null
    private val binding get() = _binding

    private lateinit var storyListItemAdapter: StoryListItemAdapter

    override fun onInitialization() {
        super.onInitialization()
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        storyListViewModel.navigator = this
        initUI()
    }

    private fun initUI() {
        binding?.rvStory?.smoothScrollToPosition(0)

        binding?.ivLogout?.setOnClickListener {
            MaterialDialog.Builder(requireContext())
                .title("Logout")
                .content("Are you sure you want to logout the app?")
                .cancelable(false)
                .positiveText("Yes")
                .onPositive { _, _ ->
                    logout()
                }
                .negativeText("No")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
        }

        binding?.ivAddStory?.setOnClickListener {
            goToAddStory()
        }

        binding?.toolbar?.setOnClickListener {
            binding?.rvStory?.smoothScrollToPosition(0)
        }

        storyListItemAdapter = StoryListItemAdapter(requireContext())
        binding?.rvStory?.layoutManager = LinearLayoutManager(context)
        binding?.rvStory?.adapter = storyListItemAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyListItemAdapter.retry()
            }
        )
    }

    override fun setLayout(): Int = R.layout.fragment_story_list

    override fun getViewModels(): StoryListViewModel = storyListViewModel

    private fun getData() {
        storyListViewModel.responseStory.observe(this) {
            storyListItemAdapter.submitData(lifecycle, it)
        }
    }

    override fun onObserveAction() {
        getData()
    }

    override fun onReadyAction() {}

    override fun logout() {
        prefs.prefUserToken = ""
        activity?.finish()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    override fun goToAddStory() {
        val intent = Intent(requireContext(), AddStoryActivity::class.java)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                getData()
                binding?.rvStory?.smoothScrollToPosition(0)
            }
        }
}