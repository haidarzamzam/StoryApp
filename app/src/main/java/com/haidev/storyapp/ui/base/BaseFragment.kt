package com.haidev.storyapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<out Any>> :
    Fragment(), CoroutineScope {

    private lateinit var job: Job
    private var _binding: T? = null
    private val binding get() = _binding
    private lateinit var rootView: View
    private val baseViewModel by lazy { getViewModels() }

    @LayoutRes
    abstract fun setLayout(): Int

    abstract fun getViewModels(): V

    open fun onInitialization() = Unit

    abstract fun onReadyAction()

    open fun onObserveAction() = Unit

    open fun onClearArguments() = Unit

    open fun onFragmentDestroyed() = Unit

    open fun getViewDataBinding() = binding

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, setLayout(), container, false)
        rootView = binding?.root!!
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitialization()
        binding?.executePendingBindings()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onReadyAction()
        onObserveAction()
    }

    override fun onPause() {
        super.onPause()
        onClearArguments()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onFragmentDestroyed()
    }
}