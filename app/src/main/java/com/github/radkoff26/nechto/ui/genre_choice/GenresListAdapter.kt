package com.github.radkoff26.nechto.ui.genre_choice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.radkoff26.nechto.R
import com.github.radkoff26.nechto.data.Genre
import com.github.radkoff26.nechto.databinding.ItemGenreBinding

class GenresListAdapter(
    genres: List<Genre>
) : RecyclerView.Adapter<GenresListAdapter.GenreViewHolder>() {
    private val genresList: MutableList<GenreInList> =
        genres.map { GenreInList(it) }.toMutableList()

    inner class GenreViewHolder(view: View) : ViewHolder(view) {
        private var binding: ItemGenreBinding = ItemGenreBinding.bind(view)

        fun bind(position: Int) {
            val genre = genresList[position]
            with(binding) {
                genreTitle.text = genre.genre.title
                updateColor(genre)
                buttonArea.setOnClickListener {
                    toggleChosenOn(position)
                    updateColor(genresList[position])
                }
            }

        }

        private fun ItemGenreBinding.updateColor(genre: GenreInList) {
            @ColorInt
            val color = with(root) {
                if (genre.isChosen) {
                    resources.getColor(com.github.radkoff26.design.R.color.primary, null)
                } else {
                    resources.getColor(com.github.radkoff26.design.R.color.white, null)
                }
            }
            genreTitle.setTextColor(color)
            buttonArea.outlineColor = color
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder =
        GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_genre,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = genresList.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getSelectedGenres(): List<Genre> =
        genresList.filter(GenreInList::isChosen).map(GenreInList::genre)

    private fun toggleChosenOn(position: Int) {
        val genre = genresList[position]
        genresList[position] = genre.copy(isChosen = !genre.isChosen)
    }
}