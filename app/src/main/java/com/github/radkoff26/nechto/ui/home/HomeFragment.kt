package com.github.radkoff26.nechto.ui.home

import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentHomeBinding
import com.github.radkoff26.nechto.ui.join.JoinRoomFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onCreateView() {
        binding.initUI()
    }

    override fun createBinding(view: View): FragmentHomeBinding = FragmentHomeBinding.bind(view)

    private fun FragmentHomeBinding.initUI() {
        joinRoomButton.setOnClickListener {
            JoinRoomFragment().show(parentFragmentManager, null)
        }
        startMatchButton.setOnClickListener {
            findNavController().navigate(R.id.from_home_to_genre_choices)
        }
    }
}
