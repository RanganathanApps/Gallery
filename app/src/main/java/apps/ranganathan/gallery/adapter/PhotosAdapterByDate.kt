package apps.ranganathan.gallery.adapter

import android.os.Handler
import android.util.Log
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.viewholders.AlbumViewHolder


class PhotosAdapterByDate(activity1: BaseActivity) : BaseSectionAdapter() {


    /*interface for data binding*/
    internal interface Binder<T> {
        fun bind(data: T)
        fun clicked(adapter: ListAdapter, index: Int)
        fun onLongClicked(adapter: ListAdapter, index: Int)
    }

    internal lateinit var itemsList: List<Album>

    fun setDataList(data: List<Album>) {
        itemsList = data
    }

    var activity = activity1

    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val movieGenre = itemsList[position].date
        val nextMovieGenre = itemsList[position + 1].date

        return movieGenre != nextMovieGenre
    }

    override fun onBindItemViewHolder(holder: AlbumViewHolder, position: Int) {
        (holder as ListAdapter.Binder<Any>).bind(holder,listItems[position])
        holder.itemView.setOnClickListener {
            //holder.clicked(, position)

            var currentPos:Int
            if (getSectionIndex(position) == 0) {
                currentPos = position
            } else {
                currentPos = position - getSectionIndex(position)
            }
            if (isSelection) {
                itemsList[currentPos].isSelected = !itemsList[currentPos].isSelected
                notifyItemChanged((currentPos))
                onItemClickListener.onItemSelected(currentPos)
            } else {
                onItemClickListener.onItemClicked(itemsList[position], position)
            }
        }
        holder.itemView.setOnLongClickListener {
            //holder.onLongClicked(this, position)
            isSelection = true
            itemsList[position].isSelected = true
            notifyDataSetChanged()
            true
        }
        /* val album = itemsList[position]

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
         holder.itemView.setOnClickListener {
             Log.w("Position:","$position ${itemsList[position].name}")
             if (isSelection) {
                 itemsList[position].isSelected = !itemsList[position].isSelected
                 notifyItemChanged(position)
                 onItemClickListener.onItemSelected(position)
             }else{
                 onItemClickListener.onItemClicked(itemsList[position], position)
             }
         }
         holder.itemView.setOnLongClickListener {
             isSelection = true
             itemsList[position].isSelected =  true
             notifyDataSetChanged()
            // onItemClickListener.onItemSelected(itemsList, position)
             true

         }*/


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