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
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.utils.PhotoSelectedListener
import kotlinx.android.synthetic.main.camera_fragment.*
import kotlinx.android.synthetic.main.photos_fragment.recyclerPhotos
import java.io.File


class CameraFragment : Fragment(){

    private val TAKE_PHOTO_REQUEST: Int = 12
    private val DIRECTORY = "/GalleryImages"
    private val DIRECTORY_UI = "GalleryImages"
    private lateinit var photoFile: File

    companion object {
        fun newInstance() = CameraFragment()
    }

    private var contentView: View? = null

    private lateinit var viewModel: CameraViewModel

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

    private fun initAlbums(files: List<Album>) {

        val adapter =  PhotosAdapter(activity!! as BaseActivity, files,photoSelctedListener = object :
            PhotoSelectedListener {
            override fun onPhotoSelected(position: Int, list: List<Album>) {
                val anotherMap = mapOf("position" to position, "tag" to "camera","directory" to DIRECTORY,"directory_ui" to DIRECTORY_UI)
                (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java, anotherMap)
            }
        })
        recyclerPhotos.layoutManager = GridLayoutManager(activity!! as BaseActivity, 3) as RecyclerView.LayoutManager?
        recyclerPhotos.setHasFixedSize(true)
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
