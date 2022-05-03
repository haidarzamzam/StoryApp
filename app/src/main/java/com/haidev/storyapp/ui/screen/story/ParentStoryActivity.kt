package com.haidev.storyapp.ui.screen.story

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityParentStoryBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.screen.login.LoginActivity
import org.koin.android.ext.android.inject

class ParentStoryActivity : BaseActivity<ActivityParentStoryBinding, ParentStoryViewModel>(),
    ParentStoryNavigator {

    private val parentStoryViewModel: ParentStoryViewModel by inject()
    private var _binding: ActivityParentStoryBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        parentStoryViewModel.navigator = this

        initUI()
    }

    private fun initUI() {
        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(
            navController
        )

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

        binding?.ivAddStory?.setOnClickListener {
            goToAddStory()
        }
    }

    override fun goToLogin() {
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun goToAddStory() {
        val intent = Intent(this, AddStoryActivity::class.java)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //TODO add request data
            }
        }

    override fun setLayout(): Int = R.layout.activity_parent_story

    override fun getViewModels(): ParentStoryViewModel = parentStoryViewModel
}