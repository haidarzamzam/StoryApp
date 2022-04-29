package com.haidev.storyapp.ui.screen.login

import android.os.Bundle
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityLoginBinding
import com.haidev.storyapp.ui.base.BaseActivity
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(),
    LoginNavigator {

    private val loginViewModel: LoginViewModel by inject()
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        loginViewModel.navigator = this
    }

    override fun setLayout(): Int = R.layout.activity_login

    override fun getViewModels(): LoginViewModel = loginViewModel
}