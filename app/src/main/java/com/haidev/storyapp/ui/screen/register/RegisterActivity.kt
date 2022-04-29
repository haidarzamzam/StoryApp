package com.haidev.storyapp.ui.screen.register

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityRegisterBinding
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.screen.login.LoginActivity
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
        initUI()
    }

    private fun initUI() {
        val ss = SpannableString("Have an account? Log in")
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                finish()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.typeface = Typeface.DEFAULT_BOLD
                ds.color = resources.getColor(R.color.white)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickSpan, 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.tvSignup?.text = ss
        binding?.tvSignup?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun setLayout(): Int = R.layout.activity_register

    override fun getViewModels(): RegisterViewModel = registerViewModel
}