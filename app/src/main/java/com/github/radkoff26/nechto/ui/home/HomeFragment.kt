package com.github.radkoff26.nechto.ui.home

import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onCreateView() {
        binding.initUI()
    }

    override fun createBinding(view: View): FragmentHomeBinding = FragmentHomeBinding.bind(view)

    private fun FragmentHomeBinding.initUI() {
        startMatchButton.setOnClickListener {
            findNavController().navigate(R.id.from_home_to_genre_choices)
        }
    }
}
