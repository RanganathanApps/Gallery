package apps.ranganathan.gallery.viewmodel

import android.os.Environment
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.configlibrary.base.BaseAppActivity.Companion.makeLog
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.viewholders.AlbumViewHolder
import apps.ranganathan.gallery.viewholders.BaseViewHolder
import apps.ranganathan.gallery.viewholders.HeaderViewHolder
import kotlinx.android.synthetic.main.photos_fragment.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraViewModel : HomeViewModel() {

    private lateinit var adapter: ListAdapter

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String,DIRECTORY:String): File {
        val timeStamp = SimpleDateFormat("MMdd_HHmmss").format(Date())
        val imageFileName = "Capture_$fileName $timeStamp.jpg"
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString() + DIRECTORY)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            makeLog("failed to create directory")
        }

        // Return the file target for the photo based on filename

        val path = mediaStorageDir.path + File.separator + imageFileName
        return File(path)
    }
    internal fun setDataToAdapter(activity:BaseActivity, files: ArrayList<Album>) {

        adapter = object : ListAdapter() {

            override fun getLayoutId(position: Int, obj: Any): Int {
                return when (obj) {
                    is Album -> {
                        R.layout.item_photos
                    }
                    else -> {
                        R.layout.item_album
                    }
                }
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                when (viewType) {
                    R.layout.item_photos -> {
                        val hol=  AlbumViewHolder(view)
                        hol.setActivity(activity ,adapter = adapter, clickable = object : BaseViewHolder.Clickable {

                            override fun clicked(adapter: ListAdapter, index: Int) {
                                var album =  adapter.listItems[index] as Album
                                if (adapter.isSelection) {
                                    album.isSelected = !album.isSelected
                                    adapter.notifyItemChanged(index)
                                    (activity as HomeActivity).makeShareDeleteToolbar(adapter.listItems)
                                } else {
                                    val anotherMap = mapOf("position" to index, "tag" to "photos")
                                    (activity ).startActivityputExtra(
                                        activity,
                                        PictureViewActivity::class.java,
                                        anotherMap
                                    )
                                }
                            }

                            override fun onLongClicked(adapter: ListAdapter, index: Int) {
                                var album =  adapter.listItems[index] as Album
                                if (!adapter.isSelection){
                                    adapter.isSelection = true
                                    adapter.notifyDataSetChanged()
                                }
                                adapter.isSelection = true
                                album.isSelected = true
                                adapter.notifyItemChanged(index)
                                (activity as HomeActivity).makeShareDeleteToolbar(adapter.listItems as List<Album>)
                            }

                        })

                        return hol
                    }
                    else -> {
                        return HeaderViewHolder(view)
                    }
                }
            }
        }

        adapter.setItems(files)
        activity.recyclerPhotos.layoutManager = GridLayoutManager(activity, 3) as RecyclerView.LayoutManager?
        activity.recyclerPhotos.hasFixedSize()
        activity.recyclerPhotos.adapter = adapter
    }
}
