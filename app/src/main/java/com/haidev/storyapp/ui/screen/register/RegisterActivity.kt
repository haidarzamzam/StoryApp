package com.haidev.storyapp.ui.screen.register

import android.os.Bundle
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityRegisterBinding
import com.haidev.storyapp.ui.base.BaseActivity
import org.koin.android.ext.android.inject

class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>(),
    RegisterNavigator {

    private val registerViewModel: RegisterViewModel by inject()
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        registerViewModel.navigator = this
    }

    override fun setLayout(): Int = R.layout.activity_register

    override fun getViewModels(): RegisterViewModel = registerViewModel
}