package apps.ranganathan.gallery.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.utils.PhotoSelectedListener
import java.util.*
import kotlin.collections.ArrayList


class PhotosAdapter(activity: BaseActivity,  photoSelctedListener: PhotoSelectedListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as PhotosAdapter.ViewHolder
        holder.bindItems(getItem(position))
        holder.imageAlbum.setOnClickListener {
            val album = getItem(position)
            if (isSelection) {
                album.isSelected = !album.isSelected
                photoSelctedListener.onItemSelected(position, photos as List<Album>)
                notifyItemChanged(position)
            } else {
                photoSelctedListener.onPhotoSelected(position, photos as List<Album>)
            }
        }
        holder.imageAlbum.setOnLongClickListener {
            /*  isSelection = true
              photos[position].isSelected = true
              photoSelctedListener.onItemSelected(position, photos)*/
            (photos as MutableList<Any>).removeAt(position)
            notifyItemRemoved(position)
            Handler().postDelayed({
                //doSomethingHere()
                notifyDataSetChanged()
            }, 1000)
            true

        }
    }

    public lateinit var holder: ViewHolder
    public var isSelection: Boolean = false
    val activity = activity
    val photoSelctedListener = photoSelctedListener

    lateinit var photos : List<Any>

    fun setitems(list:List<Any>){
        this.photos = list
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photos, parent, false)
        holder = ViewHolder(activity, itemView, this)
        return holder
    }

    //this method is binding the data on the list


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return this.photos.size
    }

    fun getItem(position: Int): Album {
        return ( this.photos[position] as  Album)
    }

    //the class is hodling the list view
    class ViewHolder(
        activity: BaseAppActivity,
        itemView: View,
        photosAdapter: PhotosAdapter
    ) : RecyclerView.ViewHolder(itemView) {
        val activity = activity
        val photosAdapter = photosAdapter
        val imageAlbum = itemView.findViewById(R.id.imgAlbum) as AppCompatImageView
        val imgAlbumSelectable = itemView.findViewById(R.id.imgAlbumSelectable) as AppCompatImageView
        val imgAlbumSelected = itemView.findViewById(R.id.imgAlbumSelected) as AppCompatImageView
        val imgAlbumOverlay = itemView.findViewById(R.id.imgAlbumOverlay) as AppCompatImageView

        fun bindItems(user: Album) {
            if (imageAlbum.drawable == null) {
                activity.loadImage(
                    user.albumUri,
                    imageAlbum,
                    R.drawable.ic_camera_alt_white_24dp
                )
            }

            if (photosAdapter.isSelection) {
                imgAlbumSelectable.visibility = VISIBLE
                imgAlbumSelected.setColorFilter(ContextCompat.getColor(activity.applicationContext, R.color.colorWhite))
                imgAlbumOverlay.setBackgroundColor(
                    ContextCompat.getColor(
                        activity.applicationContext,
                        R.color.colorTransLight
                    )
                )
                if (user.isSelected) {
                    imgAlbumSelected.visibility = VISIBLE
                    imgAlbumSelected.setColorFilter(ContextCompat.getColor(activity.applicationContext, R.color.colorWhite))
                    imgAlbumSelectable.visibility = GONE
                    imageAlbum.setPadding(25, 25, 25, 25)
                    imgAlbumOverlay.setBackgroundColor(
                        ContextCompat.getColor(
                            activity.applicationContext,
                            R.color.colorTransSelected
                        )
                    )
                } else {
                    imgAlbumSelected.visibility = GONE
                }
            } else {
                imgAlbumSelectable.visibility = GONE
                imgAlbumSelected.visibility = GONE
                imageAlbum.setPadding(0, 0, 0, 0)
                imgAlbumOverlay.setBackgroundColor(
                    ContextCompat.getColor(
                        activity.applicationContext,
                        R.color.colorTransparent
                    )
                )
            }

        }
    }
}

