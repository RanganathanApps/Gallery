package apps.ranganathan.gallery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import java.util.*

class PhotosAdapter(activity:BaseAppActivity, val userList: List<Album>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    val  activity = activity


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photos, parent, false)
        return ViewHolder(activity,v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: PhotosAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(activity: BaseAppActivity,itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mRandom = Random()
        val activity = activity
        fun bindItems(user: Album) {
            val txtPhotoName = itemView.findViewById(R.id.txtPhotoName) as TextView
            val imageAlbum  =itemView.findViewById(R.id.imgAlbum) as AppCompatImageView
            /* val params = view.layoutParams
      params.height = 60
      view.layoutParams = params*/
            imageAlbum.layoutParams.height = getRandomIntInRange(250, 150)
            imageAlbum.layoutParams.width = getRandomIntInRange(550, 350)
            val progressAlbum  =itemView.findViewById(R.id.progressAlbum) as View
            txtPhotoName.text = user.name

            activity.loadImage(user.albumUri,
                imageAlbum,R.drawable.ic_camera_alt_white_24dp,
                R.drawable.ic_camera_alt_white_24dp,progressAlbum)
        }

        private fun getRandomIntInRange(max: Int, min: Int): Int {
            return mRandom.nextInt(max - min + min) + min
        }
    }


}