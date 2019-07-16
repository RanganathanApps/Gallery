package apps.ranganathan.gallery.adapter

import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import android.view.View
import androidx.core.content.ContextCompat
import apps.ranganathan.gallery.viewholders.PhotosOrderByDateViewHolder


class AlbumsAdapterByList(activity1:BaseActivity, itemList1: List<Album>) : BaseSectionAdapter() {

    var itemsList = itemList1
    val activity = activity1
    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = itemsList[position].bucket
        val nextMovieGenre = itemsList[position + 1].bucket

        return movieGenre != nextMovieGenre
    }

    override fun onBindItemViewHolder(holder: PhotosOrderByDateViewHolder, position: Int) {
        val album = itemsList[position]

        activity.loadImage(
            album.albumUri,
            holder.photo,
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
            holder.photo.setPadding(25, 25, 25, 25)
            holder.imgAlbumOverlay.setBackgroundColor(
                ContextCompat.getColor(
                    activity.applicationContext,
                    R.color.colorTransSelected
                )
            )
        } else {
            holder.imgAlbumSelected.visibility = View.GONE
        }

        holder.photo.setOnClickListener {
            if (isSelection) {
                this.itemsList[position].isSelected = !this.itemsList[position].isSelected
                onItemClickListener.onItemSelected(position)
                notifyItemChangedAtPosition(position)
            } else {
                onItemClickListener.onItemClicked(album,position)
            }
        }
        holder.photo.setOnLongClickListener {
            isSelection = true
            this.itemsList[position].isSelected = true
            notifyDataSetChanged()
            onItemClickListener.onItemSelected( position)
            true

        }
    }

    override fun onBindSubheaderViewHolder(subheaderHolder: BaseSectionAdapter.SubheaderHolder, nextItemPosition: Int) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition)
        val context = subheaderHolder.itemView.context
        val nextMovie = itemsList[nextItemPosition]
        val sectionSize = getSectionSize(getSectionIndex(subheaderHolder.adapterPosition))
        val name = nextMovie.bucket

        subheaderHolder.mSubheaderText.text = name
    }

}