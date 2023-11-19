package com.github.radkoff26.nechto.ui.match

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.ViewModelProvider
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentMatchBinding
import com.github.radkoff26.nechto.extensions.toastMessage

class MatchFragment : BaseFragment<FragmentMatchBinding>(R.layout.fragment_match) {
    private lateinit var viewModel: MatchViewModel

    override fun onCreateView() {
        viewModel = ViewModelProvider(this)[MatchViewModel::class.java]
        observeViewModel()
        binding.initUI()
    }

    private fun observeViewModel() {
        viewModel.matchFragmentStateLiveData.observe(viewLifecycleOwner) {
            if (it == MatchFragmentState.FAILED) {
                requireContext().toastMessage("Failed to load movie!")
            }
        }
        viewModel.currentMovieLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                viewModel.nextMovie()
                return@observe
            }
            binding.bindMovie(it)
        }
    }

    private fun FragmentMatchBinding.bindMovie(moviesPair: MoviesPair) {
        topCard.setBackgroundColor(Color.parseColor(moviesPair.topMovie.poster))
        movieNameOnTop.text = moviesPair.topMovie.name
        if (moviesPair.bottomMovie != null) {
            bottomCard.setBackgroundColor(Color.parseColor(moviesPair.bottomMovie.poster))
        }
        movieNameOnBottom.text = moviesPair.bottomMovie?.name ?: "-"
    }

    private fun FragmentMatchBinding.initUI() {
        matchMotionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.offScreenLike -> {
                        viewModel.likeMovie()
                        matchMotionLayout.resetAnimationFromLike()
                    }
                    R.id.offScreenDislike -> {
                        viewModel.dislikeMovie()
                        matchMotionLayout.resetAnimationFromDislike()
                    }
                }
            }
        })
    }

    private fun MotionLayout.resetAnimationFromDislike() {
        progress = 0f
        setTransition(R.id.steady, R.id.dislike)
    }

    private fun MotionLayout.resetAnimationFromLike() {
        progress = 0f
        setTransition(R.id.steady, R.id.like)
    }

    override fun createBinding(view: View): FragmentMatchBinding = FragmentMatchBinding.bind(view)
}