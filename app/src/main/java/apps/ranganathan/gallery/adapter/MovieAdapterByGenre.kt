package apps.ranganathan.gallery.adapter

import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import android.content.Context
import java.text.DateFormat


class MovieAdapterByGenre(activity1:BaseActivity,itemList: List<Album>) : BaseMovieAdapter(itemList) {

    val activity = activity1
    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = movieList[position].date
        val nextMovieGenre = movieList[position + 1].date

        return movieGenre != nextMovieGenre
    }

    override fun onBindItemViewHolder(holder: BaseMovieAdapter.MovieViewHolder, position: Int) {
        val movie = movieList[position]

        activity.loadImage(
            movie.albumUri,
            holder.imgPhoto,
            R.drawable.ic_camera_alt_white_24dp
        )

        holder.itemView.setOnClickListener { v -> onItemClickListener.onItemClicked(movie,position) }
    }

    override fun onBindSubheaderViewHolder(subheaderHolder: BaseMovieAdapter.SubheaderHolder, nextItemPosition: Int) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition)
        val context = subheaderHolder.itemView.context
        val nextMovie = movieList[nextItemPosition]
        val sectionSize = getSectionSize(getSectionIndex(subheaderHolder.adapterPosition))
        val dateString = nextMovie.dateString

        subheaderHolder.mSubheaderText.text = dateString
    }

}