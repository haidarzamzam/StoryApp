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
import android.widget.Toast
import com.haidev.storyapp.R
import com.haidev.storyapp.data.model.LoginModel
import com.haidev.storyapp.data.model.Status
import com.haidev.storyapp.databinding.ActivityLoginBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.custom.LoadingScreen
import com.haidev.storyapp.ui.screen.register.RegisterActivity
import com.haidev.storyapp.ui.screen.story.StoryActivity
import com.haidev.storyapp.util.isValidEmail
import com.haidev.storyapp.util.isValidPassword
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

    override fun setLayout(): Int = R.layout.activity_login

    override fun getViewModels(): LoginViewModel = loginViewModel

    private fun initUI() {
        val ss = SpannableString("Don't have an account? Sign up")
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                goToRegister()
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

    override fun onReadyAction() {
        binding?.btnLogin?.setOnClickListener {
            if (!binding?.etEmail?.text.toString()
                    .isValidEmail() && !binding?.etPassword?.text.toString().isValidPassword()
            ) {
                Toast.makeText(this, "Check your warning!", Toast.LENGTH_SHORT).show()
            } else {
                val payload = LoginModel.Payload(
                    binding?.etEmail?.text.toString(),
                    binding?.etPassword?.text.toString()
                )
                loginViewModel.postLogin(payload)
            }
        }
    }

    override fun onObserveAction() {
        loginViewModel.responseLogin.observe(this, {
            when (it?.status) {
                Status.LOADING -> {
                    LoadingScreen.displayLoadingWithText(this, "Checking User. . .", false)
                }
                Status.SUCCESS -> {
                    LoadingScreen.hideLoading()
                    prefs.prefUserToken = it.data?.loginResult?.token
                    goToStory()
                }
                Status.ERROR -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> LoadingScreen.hideLoading()
            }
        })

    }

    override fun goToRegister() {
        finish()
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    override fun goToStory() {
        finish()
        startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
    }
}