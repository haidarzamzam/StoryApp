package com.haidev.storyapp.ui.screen.login


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
import com.haidev.storyapp.databinding.ActivityLoginBinding
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.screen.register.RegisterActivity
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
        initUI()
    }

    private fun initUI() {
        val ss = SpannableString("Don't have an account? Sign up")
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                finish()
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.typeface = Typeface.DEFAULT_BOLD
                ds.color = resources.getColor(R.color.white)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.tvSignup?.text = ss
        binding?.tvSignup?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun setLayout(): Int = R.layout.activity_login

    override fun getViewModels(): LoginViewModel = loginViewModel
}