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
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class AlbumsAdapter(activity: BaseActivity, val userList: List<Album>) : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    val  activity = activity

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(activity,v)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: AlbumsAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
        holder.imageAlbum.setOnClickListener {
            val album = userList.get(position)
            val anotherMap = mapOf("directory" to album.name,"position" to position,"count" to album.count,"album" to album)
            activity.startActivityputExtra(activity, PictureViewActivity::class.java,anotherMap)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(activity: BaseAppActivity,itemView: View) : RecyclerView.ViewHolder(itemView) {

        val activity = activity
        val txtAlbum = itemView.findViewById(R.id.txtAlbum) as TextView
        val txtAlbumCount  = itemView.findViewById(R.id.txtAlbumCount) as TextView
        val imageAlbum  =itemView.findViewById(R.id.imgAlbum) as AppCompatImageView

        fun bindItems(user: Album) {

            txtAlbum.text = user.name
            txtAlbumCount.text = coverWithParanthesis(user.count)


            activity.loadImage(user.albumUri,
                imageAlbum,
                R.drawable.ic_camera_alt_white_24dp)
        }

        private fun coverWithParanthesis(count: String): CharSequence? {
            return "( $count )"
        }
    }
}