package com.github.radkoff26.nechto.ui.match

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.data.Movie
import com.github.radkoff26.nechto.databinding.FragmentMatchBinding
import com.github.radkoff26.nechto.databinding.MovieLayoutBinding
import com.github.radkoff26.nechto.extensions.toastMessage
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchFragment : BaseFragment<FragmentMatchBinding>(R.layout.fragment_match) {
    private val viewModel: MatchViewModel by viewModels()

    @Volatile
    private var isLoaded = false

    override fun onCreateView() {
        val isLeader = requireArguments().getBoolean(getString(R.string.is_leader))
        val roomId = requireArguments().getString(getString(R.string.room_code))!!
        viewModel.init(roomId, isLeader, this::onFailed, this::onHavingMatch)
        observeViewModel()
        binding.initUI()
        setOnBackPressed()
    }

    override fun onStart() {
        super.onStart()
        viewModel.startWatchingMatch()
    }

    override fun onStop() {
        viewModel.stopWatchingMatch()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.leaveRoomAsLeaderIfLeader()
        }
    }

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

    private fun onHavingMatch(movie: Movie) {
        CoroutineScope(Dispatchers.Main).launch {
            findNavController().navigate(R.id.from_match_to_result, Bundle().apply {
                putString(getString(R.string.movie_title), movie.name)
                putString(getString(R.string.poster_url), movie.poster)
            })
        }
    }

    private fun onFailed(isSevere: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            requireContext().toastMessage(getString(R.string.unexpected_error))
            if (isSevere) {
                findNavController().popBackStack(R.id.home_fragment, false)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.matchFragmentStateLiveData.observe(viewLifecycleOwner) {
            if (it == MatchFragmentState.FAILED) {
                requireContext().toastMessage(getString(R.string.failed_to_load_movie))
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
        movieGenres.text = movie.genres.joinToString(separator = " - ")
        movieScore.text = movie.score.toString()
    }

    private fun FragmentMatchBinding.initUI() {
        matchMotionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.offScreenLike -> {
                        viewModel.likeMovie()
                        setSkeletonLoadingAndResetAnimationDelayed()
                    }
                    R.id.offScreenDislike -> {
                        viewModel.dislikeMovie()
                        setSkeletonLoadingAndResetAnimationDelayed()
                    }
                }
            }
        })
    }

    private fun FragmentMatchBinding.setSkeletonLoadingAndResetAnimationDelayed() {
        matchMotionLayout.progress = 0f
        matchMotionLayout.setTransition(R.id.steady, R.id.dislike)
        val bottomMovie = viewModel.currentMovieLiveData.value?.bottomMovie
        if (bottomMovie != null) {
            bindMovie(MoviesPair(bottomMovie, null))
        } else {
            topCard.showSkeleton()
        }
    }

    private fun FragmentMatchBinding.showOriginal() {
        isLoaded = true
        topCard.showOriginal()
    }

    override fun createBinding(view: View): FragmentMatchBinding = FragmentMatchBinding.bind(view)
}