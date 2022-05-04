package com.haidev.storyapp.ui.screen.story

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haidev.storyapp.R
import com.haidev.storyapp.databinding.ActivityParentStoryBinding
import com.haidev.storyapp.ui.base.BaseActivity
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
    }

    override fun setLayout(): Int = R.layout.activity_parent_story

    override fun getViewModels(): ParentStoryViewModel = parentStoryViewModel

}