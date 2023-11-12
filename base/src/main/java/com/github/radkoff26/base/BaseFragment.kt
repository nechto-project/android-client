package com.github.radkoff26.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>(@LayoutRes private val layoutId: Int) : Fragment() {
    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(
            inflater.inflate(layoutId, container, false)
        )

        onCreateView()

        return binding.root
    }

    open fun onCreateView() {}

    protected abstract fun createBinding(view: View): T
}