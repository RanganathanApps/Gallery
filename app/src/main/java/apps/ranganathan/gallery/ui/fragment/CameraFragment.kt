package apps.ranganathan.gallery.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.viewholders.AlbumViewHolder
import apps.ranganathan.gallery.viewholders.BaseViewHolder
import apps.ranganathan.gallery.viewholders.HeaderViewHolder
import apps.ranganathan.gallery.viewmodel.CameraViewModel
import kotlinx.android.synthetic.main.camera_fragment.*
import kotlinx.android.synthetic.main.photos_fragment.recyclerPhotos
import java.io.File


class CameraFragment : Fragment(){

    internal lateinit var adapter: ListAdapter
    private val TAKE_PHOTO_REQUEST: Int = 12
    private val DIRECTORY = "/GalleryImages"
    private val DIRECTORY_UI = "GalleryImages"
    private lateinit var photoFile: File

    companion object {
        fun newInstance() = CameraFragment()
    }

    private var contentView: View? = null

    private lateinit var viewModel: CameraViewModel

    fun deleteFile(context:Context){
        for (i in 0 until adapter.listItems.size) {
            if ((adapter.listItems[i] as Album).isSelected) {
                viewModel.delete(context,(adapter.listItems[i] as Album).file)
            }
        }
    }

    fun getAdapter():ListAdapter{
        return adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater!!.inflate(R.menu.menu_camera_fragment, menu)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.camera_fragment, container, false)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
        if (recyclerPhotos.adapter == null) {
            loadFiles()
        }

        txtDirectory.text = "Directory : $DIRECTORY_UI"

    }

    private fun loadFiles() {
        val files = viewModel.getImagesInFile(viewModel.getDirectory(DIRECTORY))!!
        initAlbums(viewModel.getImages(files))

    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference to access to future access
        photoFile = viewModel.getPhotoFileUri("photo", DIRECTORY)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider =
            FileProvider.getUriForFile(activity as Context, BuildConfig.APPLICATION_ID + ".provider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    private fun initAlbums(files: ArrayList<Album>) {

        setDataToAdapter(files)

    }

    private fun setDataToAdapter(files: ArrayList<Album>) {

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
                        hol.setActivity(activity as BaseActivity,adapter = adapter, clickable = object : BaseViewHolder.Clickable {

                            override fun clicked(adapter: ListAdapter, index: Int) {
                                var album =  adapter.listItems[index] as Album
                                if (adapter.isSelection) {
                                    album.isSelected = !album.isSelected
                                    adapter.notifyItemChanged(index)
                                    (activity as HomeActivity).makeShareaDeleteToolbar(adapter, null, adapter.listItems as List<Album>)
                                } else {
                                    val anotherMap = mapOf("position" to index, "tag" to "camera","directory_ui" to DIRECTORY_UI,"directory" to DIRECTORY)
                                    (activity as BaseActivity).startActivityputExtra(
                                        activity as BaseActivity,
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
                                (activity as HomeActivity).makeShareaDeleteToolbar(adapter, null, adapter.listItems as List<Album>)
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
        recyclerPhotos.layoutManager = GridLayoutManager(activity, 3)
        recyclerPhotos.hasFixedSize()
        recyclerPhotos.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK) {

                if (photoFile != null && ::photoFile.isInitialized)
                    photoFile.createNewFile()
               viewModel.setMediaMounted(activity as Context,photoFile.path)
                loadFiles()

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_camera -> {
                onLaunchCamera()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
