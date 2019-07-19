package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.InfoViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_info.*
import java.io.File
import android.graphics.BitmapFactory
import android.net.Uri



class InfoActivity : BaseActivity() {

    private lateinit var infoViewModel: InfoViewModel

    private lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)

        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel::class.java)
        if (intent!!.extras != null) {
            if (intent!!.extras!!.containsKey("album")) {
                album = intent!!.extras!!.getSerializable("album") as Album
                setToolBarTitle(album.file.name)
            }
        }

        txtPath.text = infoViewModel.getFilePath(album.file)
        txtLastModified.text = infoViewModel.getFileDate(album.file)
        txtLastModifiedTime.text = infoViewModel.getFileTime(album.file)
        txtSize.text = infoViewModel.getFileSize(album.file)
        getIMGSize(album.file)

        Picasso.get().load(album.albumUri).into(imgInfo, object : Callback {
            override fun onError(e: Exception?) {

            }

            override fun onSuccess() {
                //imageAlbum.heightRatio = (imageAlbum.height /4).toDouble()
                //imageAlbum.layoutParams.height = imageAlbum.height/2
                //imageAlbum.layoutParams.width = imageAlbum.width/2
            }


        })

    }
    private fun getIMGSize(file: File) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        txtWidth.text = ""+imageWidth
        txtHeight.text = ""+imageHeight

    }

}
