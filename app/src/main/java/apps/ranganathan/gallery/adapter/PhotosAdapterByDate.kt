package apps.ranganathan.gallery.adapter

import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class PhotosAdapterByDate(activity1:BaseActivity) : BaseSectionAdapter() {


   internal lateinit var itemsList:List<Album>

    fun setDataList(data: List<Album>){
        itemsList = data
    }

    val activity = activity1
    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = itemsList[position].date
        val nextMovieGenre = itemsList[position + 1].date

        return movieGenre != nextMovieGenre
    }

    override fun onBindItemViewHolder(holder: MovieViewHolder, position: Int) {
        val album = itemsList[position]

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
        holder.itemView.setOnClickListener {
            if (isSelection) {
                onItemClickListener.onItemSelected(itemsList, position)
            }else{
                onItemClickListener.onItemClicked(itemsList[position], position)
            }
        }
        holder.itemView.setOnLongClickListener {
            isSelection = true
            itemsList[position].isSelected =  true
            notifyItemChanged(position)
           // onItemClickListener.onItemSelected(itemsList, position)
            true

        }


    }

    override fun onBindSubheaderViewHolder(subheaderHolder: BaseSectionAdapter.SubheaderHolder, nextItemPosition: Int) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition)
        val nextMovie = itemsList[nextItemPosition]
        val dateString = nextMovie.dateString

        subheaderHolder.mSubheaderText.text = dateString
    }

    fun deleteItems() {
        var temp = arrayListOf<Any>()
        for (i in 0 until itemsList.size) {
            if ((itemsList[i] as Album).isSelected)
                temp.add(itemsList[i])
        }
        (this.itemsList as MutableList<Any>).removeAll(temp)
        Handler().postDelayed({
            //doSomethingHere()
            notifyDataSetChanged()
        }, 1000)
    }



}