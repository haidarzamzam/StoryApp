package com.haidev.storyapp.ui.screen.story

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityStoryBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.screen.login.LoginActivity
import com.haidev.storyapp.util.LoadingStateAdapter
import org.koin.android.ext.android.inject

class StoryActivity : BaseActivity<ActivityStoryBinding, StoryViewModel>(),
    StoryNavigator {

    private val storyViewModel: StoryViewModel by inject()
    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding

    private lateinit var storyItemAdapter: StoryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        initUI()
    }

    private fun initUI() {
        storyItemAdapter = StoryItemAdapter(this)
        binding?.rvStory?.layoutManager = LinearLayoutManager(this)
        binding?.rvStory?.adapter = storyItemAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyItemAdapter.retry()
            }
        )
        binding?.ivLogout?.setOnClickListener {
            MaterialDialog.Builder(this)
                .title("Logout")
                .content("Are you sure you want to logout the app?")
                .cancelable(false)
                .positiveText("Yes")
                .onPositive { _, _ ->
                    prefs.prefUserToken = ""
                    goToLogin()
                }
                .negativeText("No")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()

        }

        binding?.fabAddStory?.setOnClickListener {
            goToAddStory()
        }
    }

    override fun setLayout(): Int = R.layout.activity_story

    override fun getViewModels(): StoryViewModel = storyViewModel

    override fun goToLogin() {
        finish()
        startActivity(Intent(this@StoryActivity, LoginActivity::class.java))
    }

    override fun goToAddStory() {
        val intent = Intent(this@StoryActivity, AddStoryActivity::class.java)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //getData()
            }
        }

    override fun onObserveAction() {
        storyViewModel.responseStory.observe(this, {
            Log.d("CHECKK", it.toString())
            storyItemAdapter.submitData(lifecycle, it)
        })
    }
}