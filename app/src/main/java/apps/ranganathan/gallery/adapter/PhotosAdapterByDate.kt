package apps.ranganathan.gallery.adapter

import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import java.text.DateFormat


class PhotosAdapterByDate(activity1:BaseActivity, itemList: List<Album>) : BaseMovieAdapter(itemList) {

    val activity = activity1
    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = movieList[position].date
        val nextMovieGenre = movieList[position + 1].date

        return movieGenre != nextMovieGenre
    }

    override fun onBindItemViewHolder(holder: BaseMovieAdapter.MovieViewHolder, position: Int) {
        val album = movieList[position]

        activity.loadImage(
            album.albumUri,
            holder.imgPhoto,
            R.drawable.ic_camera_alt_white_24dp
        )

        if (isSelection) {
            holder.imgAlbumSelectable.visibility = View.VISIBLE
            holder.imgAlbumSelected.setColorFilter(ContextCompat.getColor(activity.applicationContext, R.color.colorWhite))
            holder.imgAlbumOverlay.setBackgroundColor(
                ContextCompat.getColor(
                    activity.applicationContext,
                    R.color.colorTransLight
                )
            )
        } else {
            holder.imgAlbumSelectable.visibility = View.GONE
        }
        if (album.isSelected) {
            holder.imgAlbumSelected.visibility = View.VISIBLE
            holder.imgAlbumSelected.setColorFilter(ContextCompat.getColor(activity.applicationContext, R.color.colorWhite))
            holder.imgAlbumSelectable.visibility = View.GONE
            holder.imgPhoto.setPadding(25, 25, 25, 25)
            holder.imgAlbumOverlay.setBackgroundColor(
                ContextCompat.getColor(
                    activity.applicationContext,
                    R.color.colorTransSelected
                )
            )
        } else {
            holder.imgAlbumSelected.visibility = View.GONE
        }

        holder.imgPhoto.setOnClickListener {
            if (isSelection) {
                this.movieList[position].isSelected = !this.movieList[position].isSelected
                onItemClickListener.onItemSelected(movieList,position)
                notifyItemChangedAtPosition(position)
            } else {
                onItemClickListener.onItemClicked(album,position)
            }
        }
        holder.imgPhoto.setOnLongClickListener {
            isSelection = true
            this.movieList[position].isSelected = true
            notifyDataSetChanged()
            onItemClickListener.onItemSelected(movieList, position)
            true

        }
    }

    override fun onBindSubheaderViewHolder(subheaderHolder: BaseMovieAdapter.SubheaderHolder, nextItemPosition: Int) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition)
        val nextMovie = movieList[nextItemPosition]
        val dateString = nextMovie.dateString

        subheaderHolder.mSubheaderText.text = dateString
    }

}