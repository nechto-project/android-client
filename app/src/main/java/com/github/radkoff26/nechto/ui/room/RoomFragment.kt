package com.github.radkoff26.nechto.ui.room

import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import androidx.core.content.getSystemService
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentRoomBinding
import com.github.radkoff26.nechto.extensions.toastMessage
import kotlin.random.Random

class RoomFragment : BaseFragment<FragmentRoomBinding>(R.layout.fragment_room) {
    private val code = generateRandomCode()
    private var clipboardManager: ClipboardManager? = null

    override fun onCreateView() {
        clipboardManager = requireContext().getSystemService()
        binding.initUI()
    }

    private fun FragmentRoomBinding.initUI() {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        codeLayout.roomCode.text = code
        codeLayout.copyCodeButton.setOnClickListener {
            copyCodeToClipboard(code)
        }
        startButton.setOnClickListener {
            findNavController().navigate(R.id.from_room_to_match)
        }
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

    // TODO: change to appropriate method
    private fun generateRandomCode(): String {
        return buildString {
            repeat(6) {
                if (Random.nextBoolean()) {
                    var char = 'a' + Random.nextInt(0, 26)
                    val capitalize = Random.nextBoolean()
                    if (capitalize) {
                        char = char.uppercaseChar()
                    }
                    append(char)
                } else {
                    val digit = Random.nextInt(10)
                    append(digit)
                }
            }
        }
    }

    override fun createBinding(view: View): FragmentRoomBinding = FragmentRoomBinding.bind(view)
}
