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
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager






class PhotosAdapter(activity: BaseActivity, val userList: List<Album>) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    val  activity = activity
    private val mRandom = Random()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photos, parent, false)

       /* val lp = itemView.layoutParams as GridLayoutManager.LayoutParams
        lp.height = parent.measuredHeight / 4
        itemView.layoutParams = lp*/
        return ViewHolder(activity,itemView)

    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: PhotosAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])


        holder.imageAlbum.setOnClickListener {
            val anotherMap = mapOf("position" to position,"tag" to "photos")
            activity.startActivityputExtra(activity,PictureViewActivity::class.java,anotherMap)
        }
       /* val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getRandomIntInRange(56,91)%2==0
        holder.itemView.layoutParams = layoutParams*/
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //the class is hodling the list view
    class ViewHolder(activity: BaseAppActivity,itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mRandom = Random()
        val activity = activity
        private val txtPhotoName = itemView.findViewById(R.id.txtPhotoName) as TextView
        val imageAlbum  =itemView.findViewById(R.id.imgAlbum) as AppCompatImageView

        fun bindItems(user: Album) {



            /*val params = view.layoutParams
            params.height = 60
            view.layoutParams = params*/


            activity.makeLog("Date : ", user.file.lastModified().toString())
            txtPhotoName.text = user.name

            if (imageAlbum.drawable==null) {
                //imageAlbum.layoutParams.height = getRandomIntInRange(250, 250)
                //imageAlbum.layoutParams.width = getRandomIntInRange(550, 350)

              /*  Picasso.get().load(user.albumUri)
                    .fit()
                    .centerCrop()
                    .into(imageAlbum)*/
                activity.loadImage(user.albumUri,
                    imageAlbum,
                    R.drawable.ic_camera_alt_white_24dp)
            }
        }

        private fun getRandomIntInRange(max: Int, min: Int): Int {
            return mRandom.nextInt(max - min + min) + min
        }
    }

    private fun getRandomIntInRange(max: Int, min: Int): Int {
        return mRandom.nextInt(max - min + min) + min
    }


}

