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
import android.widget.Toast
import com.haidev.storyapp.R
import com.haidev.storyapp.data.model.RegisterModel
import com.haidev.storyapp.data.model.Status
import com.haidev.storyapp.databinding.ActivityRegisterBinding
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.custom.LoadingScreen
import com.haidev.storyapp.ui.screen.login.LoginActivity
import com.haidev.storyapp.util.isValidEmail
import com.haidev.storyapp.util.isValidName
import com.haidev.storyapp.util.isValidPassword
import org.koin.android.ext.android.inject
import java.util.*

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

    override fun setLayout(): Int = R.layout.activity_register

    override fun getViewModels(): RegisterViewModel = registerViewModel

    private fun initUI() {
        val ss = SpannableString(resources.getString(R.string.have_an_account_log_in))
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                goToLogin(false)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.typeface = Typeface.DEFAULT_BOLD
                ds.color = resources.getColor(R.color.white)
                ds.isUnderlineText = false
            }
        }
        when (Locale.getDefault().language) {
            "en" -> ss.setSpan(clickSpan, 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            "in" -> ss.setSpan(clickSpan, 18, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding?.tvSignup?.text = ss
        binding?.tvSignup?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onReadyAction() {
        binding?.btnRegister?.setOnClickListener {
            if (!binding?.etEmail?.text.toString()
                    .isValidEmail() && !binding?.etName?.text.toString()
                    .isValidName() && !binding?.etPassword?.text.toString().isValidPassword()
            ) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.toast_check_your_warning),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val payload = RegisterModel.Payload(
                    binding?.etEmail?.text.toString(),
                    binding?.etName?.text.toString(),
                    binding?.etPassword?.text.toString()
                )
                registerViewModel.postRegister(payload)
            }
        }
    }

    override fun onObserveAction() {
        registerViewModel.responseRegister.observe(this) {
            when (it?.status) {
                Status.LOADING ->
                    LoadingScreen.displayLoadingWithText(
                        this,
                        resources.getString(R.string.toast_creating_user),
                        false
                    )
                Status.SUCCESS -> {
                    LoadingScreen.hideLoading()
                    goToLogin(true)
                }
                Status.ERROR -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(this, it.throwable.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> LoadingScreen.hideLoading()
            }
        }
    }


    override fun goToLogin(isRegistered: Boolean) {
        if (isRegistered) Toast.makeText(
            this,
            resources.getString(R.string.toast_register_successfully),
            Toast.LENGTH_SHORT
        ).show()
        finish()
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
}