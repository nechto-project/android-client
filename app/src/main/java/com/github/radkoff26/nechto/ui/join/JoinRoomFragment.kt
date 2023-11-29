package com.github.radkoff26.nechto.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentJoinRoomBinding
import com.github.radkoff26.nechto.exceptions.LoadingException
import com.github.radkoff26.nechto.extensions.toastMessage
import com.github.radkoff26.nechto.repository.RoomRepository
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class JoinRoomFragment : DialogFragment() {

    @Inject
    lateinit var roomRepository: RoomRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentJoinRoomBinding.bind(
            inflater.inflate(R.layout.fragment_join_room, container, false)
        ).also {
            it.setupUI()
        }.root
    }

    private fun FragmentJoinRoomBinding.setupUI() {
        codeInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                codeInputLayout.isErrorEnabled = false
            }
        }
        joinButton.setOnClickListener {
            lifecycleScope.launch {
                joinButton.isEnabled = false
                val code = codeInput.text.toString()
                val canJoin = withContext(Dispatchers.IO) {
                    try {
                        roomRepository.joinRoom(code)
                    } catch (e: LoadingException) {
                        null
                    }
                }
                if (canJoin == null) {
                    requireContext().toastMessage(getString(R.string.error_while_loading))
                } else {
                    if (canJoin) {
                        findNavController().navigate(R.id.from_home_to_match, Bundle().apply {
                            putString(getString(R.string.room_code), code)
                        })
                        dismiss()
                    } else {
                        codeInputLayout.setCodeError()
                    }
                }
                joinButton.isEnabled = true
            }

        }
    }

    private fun TextInputLayout.setCodeError() {
        error = getString(R.string.room_not_exists)
        isErrorEnabled = true
    }
}