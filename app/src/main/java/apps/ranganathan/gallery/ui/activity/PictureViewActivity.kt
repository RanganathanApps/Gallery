package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ViewPagerAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import apps.ranganathan.gallery.viewmodel.PictureViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_picture_view.*
import kotlinx.android.synthetic.main.content_picture_view.*
import java.io.File
import java.io.Serializable

class PictureViewActivity : BaseActivity() {

    private lateinit var pictureViewModel: PictureViewModel
   lateinit var  userList: List<Album>
    private var position: Int = 0


    private lateinit var directory: String
    private lateinit var count: String

    private lateinit var files: ArrayList<File>

    private lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_view)
        setAppBar("Photo")

        pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel::class.java)
        if ( intent!!.extras!=null) {
            if (intent!!.extras!!.containsKey("album")){
                album = intent!!.extras!!.getSerializable("album") as Album
                directory = album.name
                count = album.count
                setAppBar("$directory (${album.count} items)")
                files = pictureViewModel.getImagesInFile(pictureViewModel.getDirectory(album.path))
                userList = pictureViewModel.getImages(files)
            } else {
                userList = intent!!.extras!!.getSerializable("photos") as List<Album>

                position = intent!!.extras!!.getInt("position")
            }


            viewpagerPhotos.adapter = ViewPagerAdapter(this, userList)
            viewpagerPhotos.setCurrentItem(position, true)
        }



    }

}
