package com.github.radkoff26.nechto.ui.match

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.ViewModelProvider
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.NechtoApplication
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.data.Genre
import com.github.radkoff26.nechto.data.Movie
import com.github.radkoff26.nechto.databinding.FragmentMatchBinding
import com.github.radkoff26.nechto.databinding.MovieLayoutBinding
import com.github.radkoff26.nechto.extensions.toastMessage
import com.squareup.picasso.Picasso

class MatchFragment : BaseFragment<FragmentMatchBinding>(R.layout.fragment_match) {
    private lateinit var viewModel: MatchViewModel

    override fun onCreateView() {
        val dataSource = (requireActivity().application as NechtoApplication).movieDataSource
        viewModel = ViewModelProvider(
            this,
            MatchViewModel.ViewModelFactory(dataSource)
        )[MatchViewModel::class.java]
        observeViewModel()
        binding.initUI()
    }

    private fun observeViewModel() {
        viewModel.matchFragmentStateLiveData.observe(viewLifecycleOwner) {
            if (it == MatchFragmentState.FAILED) {
                requireContext().toastMessage("Failed to load movie!")
            } else if (it == MatchFragmentState.NOT_LOADED) {
                binding.rootSkeleton.showSkeleton()
            }
        }
        viewModel.currentMovieLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                viewModel.nextMovie()
                return@observe
            }
            binding.bindMovie(it)
            binding.showOriginal()
        }
    }

    private fun FragmentMatchBinding.bindMovie(moviesPair: MoviesPair) {
        topMovie.bindAsTopMovie(moviesPair.topMovie)
        bottomMovie.bindAsBottomMovie(moviesPair.bottomMovie)
        rootSkeleton.showOriginal()
    }

    private fun MovieLayoutBinding.bindAsTopMovie(movie: Movie) {
        acceptMovieButton.setOnClickListener {
            binding.matchMotionLayout.transitionToState(R.id.like)
        }
        rejectMovieButton.setOnClickListener {
            binding.matchMotionLayout.transitionToState(R.id.dislike)
        }
        bindCommonMovie(movie)
    }

    private fun MovieLayoutBinding.bindAsBottomMovie(movie: Movie?) {
        movie ?: return
        bindCommonMovie(movie)
    }

    private fun MovieLayoutBinding.bindCommonMovie(movie: Movie) {
        if (movie.poster != null) {
            Picasso.get().load(movie.poster).into(movieCover)
        }
        movieTitle.text = movie.name
        movieDescription.text = movie.description
        movieGenres.text = movie.genres.joinToString(separator = " - ", transform = Genre::name)
        movieScore.text = movie.score.toString()
    }

    private fun FragmentMatchBinding.initUI() {
        matchMotionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.offScreenLike -> {
                        viewModel.likeMovie()
                        setSkeletonLoadingAndResetAnimation()
                    }
                    R.id.offScreenDislike -> {
                        viewModel.dislikeMovie()
                        setSkeletonLoadingAndResetAnimation()
                    }
                }
            }
        })
    }

    private fun FragmentMatchBinding.setSkeletonLoadingAndResetAnimation() {
        topCard.showSkeleton()
        matchMotionLayout.progress = 0f
        matchMotionLayout.setTransition(R.id.steady, R.id.dislike)
    }

    private fun FragmentMatchBinding.showOriginal() {
        topCard.showOriginal()
    }

    override fun createBinding(view: View): FragmentMatchBinding = FragmentMatchBinding.bind(view)
}