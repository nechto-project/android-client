package com.github.radkoff26.nechto.ui.result

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.getSystemService
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentMatchResultBinding
import com.squareup.picasso.Picasso

class MatchResultFragment :
    BaseFragment<FragmentMatchResultBinding>(R.layout.fragment_match_result) {
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreateView() {
        clipboardManager = requireContext().getSystemService()!!

        val movieTitle = requireArguments().getString(getString(R.string.movie_title))!!
        val posterUrl = requireArguments().getString(getString(R.string.poster_url))
        binding.initUI(movieTitle, posterUrl)

        setOnBackPressed()
    }

    override fun createBinding(view: View): FragmentMatchResultBinding =
        FragmentMatchResultBinding.bind(view)

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.home_fragment, false)
                }
            }
        )
    }

    private fun FragmentMatchResultBinding.initUI(movieTitle: String, posterUrl: String?) {
        movieTitleTextView.text = movieTitle
        if (posterUrl != null) {
            Picasso.get().load(posterUrl).into(movieCoverImageView)
        }
        finishButton.setOnClickListener {
            findNavController().popBackStack(R.id.home_fragment, false)
        }
        movieTitleTextView.setOnClickListener {
            copyMovieTitle(movieTitle)
        }
    }

    private fun copyMovieTitle(title: String) {
        clipboardManager.setPrimaryClip(
            ClipData(
                ClipDescription("Movie Title", emptyArray()),
                ClipData.Item(title)
            )
        )
    }
}
