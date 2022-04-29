package com.haidev.storyapp.ui.screen.splash

import android.content.Intent
import android.os.Bundle
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivitySplashBinding
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.screen.login.LoginActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(),
    SplashNavigator {

    private val splashViewModel: SplashViewModel by inject()
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        splashViewModel.navigator = this
    }

    override fun setLayout(): Int = R.layout.activity_splash

    override fun getViewModels(): SplashViewModel = splashViewModel

    override fun onResume() {
        super.onResume()
        launch {
            splashViewModel.displaySplashAsync().await()
        }
    }

    override fun navigateToLogin() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}