package apps.ranganathan.gallery.adapter

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


class PhotosAdapter(activity: BaseActivity, val userList: List<Album>, photoSelctedListener: PhotoSelectedListener) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    public var isSelection: Boolean = false
    val activity = activity
    val photoSelctedListener = photoSelctedListener


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photos, parent, false)
        return ViewHolder(activity, itemView, this)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.imageAlbum.setOnClickListener {
            if (isSelection) {
                if (userList[position].isSelected) {
                    this.userList[position].isSelected = false
                } else {
                    photoSelctedListener.onItemSelected(position, userList)
                    this.userList[position].isSelected = true
                }
                notifyItemChanged(position)
            } else {
                photoSelctedListener.onPhotoSelected(position, userList)
            }
        }
        holder.imageAlbum.setOnLongClickListener {
            isSelection = true
            photoSelctedListener.onItemSelected(position, userList)
            userList[position].isSelected = true
            notifyDataSetChanged()
            true

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    //the class is hodling the list view
    class ViewHolder(
        activity: BaseAppActivity,
        itemView: View,
        photosAdapter: PhotosAdapter
    ) : RecyclerView.ViewHolder(itemView) {
        private val mRandom = Random()
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
            } else {
                imgAlbumSelectable.visibility = GONE
            }
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
        }
    }
}

