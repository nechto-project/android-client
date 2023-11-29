package com.github.radkoff26.nechto.ui.genre_choice

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.LoadableData
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.data.Genre
import com.github.radkoff26.nechto.databinding.FragmentGenreChoiceBinding
import com.github.radkoff26.nechto.extensions.toastMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GenreChoiceFragment :
    BaseFragment<FragmentGenreChoiceBinding>(R.layout.fragment_genre_choice) {

    private lateinit var adapter: GenresListAdapter
    private val viewModel: GenreChoiceViewModel by viewModels()

    override fun onCreateView() {
        binding.initButtons()
        binding.initUIAccordingToData(
            viewModel.genresDataLiveData.value!!
        )
        viewModel.genresDataLiveData.observe(viewLifecycleOwner) {
            binding.initUIAccordingToData(it)
        }
        viewModel.loadGenresIfNotYet()
    }

    override fun createBinding(view: View): FragmentGenreChoiceBinding =
        FragmentGenreChoiceBinding.bind(view)

    private fun FragmentGenreChoiceBinding.initButtons() {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        continueButton.setOnClickListener {
            lifecycleScope.launch {
                val genres = adapter.getSelectedGenres()
                if (genres.isEmpty()) {
                    requireContext().toastMessage(getString(R.string.no_genres_chosen))
                    return@launch
                }
                val code = viewModel.createRoom(genres)
                if (code == null) {
                    requireContext().toastMessage(getString(R.string.failed_to_create_room))
                } else {
                    findNavController().navigate(R.id.from_genre_choices_to_room, Bundle().apply {
                        putString(getString(R.string.room_code), code)
                    })
                }
            }
        }
    }

    private fun FragmentGenreChoiceBinding.initUIAccordingToData(genresData: LoadableData<List<Genre>>) {
        when (genresData) {
            is LoadableData.Loading -> {
                showLoader()
            }
            is LoadableData.Loaded -> {
                showList(genresData.data)
            }
            is LoadableData.Failed -> {
                requireContext().toastMessage(getString(R.string.error_while_loading))
            }
        }
    }

    private fun FragmentGenreChoiceBinding.showLoader() {
        loader.visibility = View.VISIBLE
        genresList.visibility = View.GONE
    }

    private fun FragmentGenreChoiceBinding.showList(genres: List<Genre>) {
        adapter = GenresListAdapter(genres)
        genresList.adapter = adapter
        genresList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                setDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.list_vertical_divider,
                        null
                    )!!
                )
            }
        )
        genresList.visibility = View.VISIBLE
        loader.visibility = View.GONE
    }
}