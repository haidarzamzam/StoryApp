package com.haidev.storyapp.ui.screen.account

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.FragmentAccountBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseFragment
import com.haidev.storyapp.ui.screen.login.LoginActivity
import org.koin.android.ext.android.inject

class AccountFragment : BaseFragment<FragmentAccountBinding, AccountViewModel>(),
    AccountNavigator {

    private val accountViewModel: AccountViewModel by inject()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding

    override fun onInitialization() {
        super.onInitialization()
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        accountViewModel.navigator = this
        initUI()
    }

    private fun initUI() {
        binding?.ivProfile?.let {
            Glide.with(requireContext()).load("https://hai-dev.com/img/profile_google.png")
                .into(it)
        }

        binding?.llAppChangeLanguage?.setOnClickListener {
            changeLanguage()
        }

        binding?.llAppInfo?.setOnClickListener {
            val libVersionName: String = com.haidev.storyapp.BuildConfig.VERSION_NAME
            val libVersionCode: String = com.haidev.storyapp.BuildConfig.VERSION_CODE.toString()

            Toast.makeText(
                requireContext(),
                "StoryApp $libVersionName ($libVersionCode)",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding?.llLogout?.setOnClickListener {
            MaterialDialog.Builder(requireContext())
                .title(resources.getString(R.string.option_logout))
                .content(resources.getString(R.string.desc_logout))
                .cancelable(false)
                .positiveText(resources.getString(R.string.yes))
                .onPositive { _, _ ->
                    logout()
                }
                .negativeText(resources.getString(R.string.no))
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    override fun setLayout(): Int = R.layout.fragment_account
    override fun getViewModels(): AccountViewModel = accountViewModel

    override fun onReadyAction() {
    }

    override fun logout() {
        prefs.prefUserToken = ""
        activity?.finish()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    override fun changeLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }
}