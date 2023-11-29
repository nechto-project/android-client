package com.github.radkoff26.nechto.ui.room

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentRoomBinding
import com.github.radkoff26.nechto.extensions.toastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment : BaseFragment<FragmentRoomBinding>(R.layout.fragment_room) {
    private var clipboardManager: ClipboardManager? = null

    private val viewModel: RoomViewModel by viewModels()

    override fun onCreateView() {
        clipboardManager = requireContext().getSystemService()
        val code = requireArguments().getString(getString(R.string.room_code))!!
        viewModel.init(code)
        viewModel.participantsCountLiveData.observe(viewLifecycleOwner) {
            binding.participantsCountTextView.text = getString(
                R.string.participants_count,
                it
            )
        }
        binding.initUI(code)
    }

    override fun onStart() {
        super.onStart()
        viewModel.startPollingForCount()
    }

    override fun onStop() {
        viewModel.stopPollingForCount()
        super.onStop()
    }

    private fun FragmentRoomBinding.initUI(code: String) {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        codeLayout.roomCode.text = code
        codeLayout.copyCodeButton.setOnClickListener {
            copyCodeToClipboard(code)
        }
        startButton.setOnClickListener {
            findNavController().navigate(R.id.from_room_to_match, Bundle().apply {
                putString(getString(R.string.room_code), code)
            })
        }
        participantsCountTextView.text = getString(
            R.string.participants_count,
            viewModel.participantsCountLiveData.value
        )
    }

    private fun copyCodeToClipboard(code: String) {
        clipboardManager?.setPrimaryClip(
            ClipData(
                "Room code",
                emptyArray(),
                ClipData.Item(code)
            )
        )
        requireContext().toastMessage(getString(R.string.code_copied))
    }

    override fun createBinding(view: View): FragmentRoomBinding = FragmentRoomBinding.bind(view)
}
