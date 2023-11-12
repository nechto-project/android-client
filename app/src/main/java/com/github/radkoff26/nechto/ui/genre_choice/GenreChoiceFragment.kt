package com.github.radkoff26.nechto.ui.genre_choice

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.radkoff26.base.BaseFragment
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.databinding.FragmentGenreChoiceBinding

class GenreChoiceFragment :
    BaseFragment<FragmentGenreChoiceBinding>(R.layout.fragment_genre_choice) {

    override fun onCreateView() {
        binding.initUI()
    }

    override fun createBinding(view: View): FragmentGenreChoiceBinding =
        FragmentGenreChoiceBinding.bind(view)

    private fun FragmentGenreChoiceBinding.initUI() {
        genresList.adapter = GenresListAdapter(GENRES.toMutableList())
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
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private val GENRES = listOf(
            "Adventure",
            "Drama",
            "Comedy",
            "Cartoon"
        )
    }
}