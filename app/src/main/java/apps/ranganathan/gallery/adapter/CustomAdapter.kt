package apps.ranganathan.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album

class CustomAdapter(activity:BaseAppActivity,val userList: ArrayList<Album>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val  activity = activity

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(activity,v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(activity: BaseAppActivity,itemView: View) : RecyclerView.ViewHolder(itemView) {

        val activity = activity
        fun bindItems(user: Album) {
            val txtAlbum = itemView.findViewById(R.id.txtAlbum) as TextView
            val txtAlbumCount  = itemView.findViewById(R.id.txtAlbumCount) as TextView
            val imageAlbum  =itemView.findViewById(R.id.imgAlbum) as AppCompatImageView
            val progressAlbum  =itemView.findViewById(R.id.progressAlbum) as View
            txtAlbum.text = user.name
            txtAlbumCount.text = user.count
            activity.loadImage("https://images.pexels.com/photos/1386604/pexels-photo-1386604.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                imageAlbum,R.drawable.ic_camera_alt_white_24dp,
                R.drawable.ic_camera_alt_white_24dp,progressAlbum)
        }
    }
}