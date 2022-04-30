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
import com.haidev.storyapp.data.model.Resource
import com.haidev.storyapp.data.model.Status
import com.haidev.storyapp.databinding.ActivityLoginBinding
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.custom.LoadingScreen
import com.haidev.storyapp.ui.screen.register.RegisterActivity
import com.haidev.storyapp.util.isValidEmail
import com.haidev.storyapp.util.isValidPassword
import com.haidev.storyapp.util.observe
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
                with(loginViewModel) {
                    observe(responseLogin, ::handleLogin)
                }
            }
        }
    }

    private fun handleLogin(it: Resource<LoginModel.Response>?) {
        when (it?.status) {
            Status.LOADING -> {
                LoadingScreen.hideLoading()
                LoadingScreen.displayLoadingWithText(this, "Checking User. . .", false)
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
    }

    override fun setLayout(): Int = R.layout.activity_login

    override fun getViewModels(): LoginViewModel = loginViewModel
}