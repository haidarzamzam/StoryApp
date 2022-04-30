package com.haidev.storyapp.ui.screen.story

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.haidev.storyapp.R
import com.haidev.storyapp.data.model.Status
import com.haidev.storyapp.databinding.ActivityStoryBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.custom.LoadingScreen
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
    }

    override fun setLayout(): Int = R.layout.activity_story

    override fun getViewModels(): StoryViewModel = storyViewModel

    override fun goToLogin() {
        finish()
        startActivity(Intent(this@StoryActivity, LoginActivity::class.java))
    }

    override fun onReadyAction() {
        storyViewModel.getStory()
    }

    override fun onObserveAction() {
        storyViewModel.responseStory.observe(this, {
            when (it?.status) {
                Status.LOADING -> {
                    LoadingScreen.hideLoading()
                    LoadingScreen.displayLoadingWithText(this, "Load Story. . .", false)
                }
                Status.SUCCESS -> {
                    LoadingScreen.hideLoading()
                }
                Status.ERROR -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(this, it.throwable.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> LoadingScreen.hideLoading()
            }
        })
    }
}