package com.haidev.storyapp.ui.screen.story

import android.content.Intent
import android.os.Bundle
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityStoryBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.screen.login.LoginActivity
import org.koin.android.ext.android.inject

class StoryActivity : BaseActivity<ActivityStoryBinding, StoryViewModel>(),
    StoryNavigator {

    private val storyViewModel: StoryViewModel by inject()
    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        storyViewModel.navigator = this
        initUI()
    }

    private fun initUI() {
        binding?.ivLogout?.setOnClickListener {
            prefs.prefUserToken = ""
            goToLogin()
        }
    }

    override fun setLayout(): Int = R.layout.activity_story

    override fun getViewModels(): StoryViewModel = storyViewModel

    override fun goToLogin() {
        finish()
        startActivity(Intent(this@StoryActivity, LoginActivity::class.java))
    }
}