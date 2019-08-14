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
import android.view.View.GONE
import java.net.URI


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
                if (!album.albumUri.startsWith("file")){
                    album.albumUri =  Uri.parse(album.albumUri).toString()

                }
                if (getIMGSize(album.file)!=null){
                    infoSizeLy.visibility = GONE
                    infoDimensionLy.visibility = GONE
                }else{
                    txtSize.text = infoViewModel.getFileSize(album.file)
                    txtWidth.text = getIMGSize(album.file)
                }

                txtPath.text = infoViewModel.getFilePath(album.file)
                txtLastModified.text = infoViewModel.getFileDate(album.file)
                txtLastModifiedTime.text = infoViewModel.getFileTime(album.file)
                setToolBarTitle(album.file.name)
                loadImage(album.albumUri,
                    imgInfo,
                    R.drawable.ic_camera_alt_white_24dp)
            }
        }

    }


    private fun getIMGSize(file: File): String? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        if (imageWidth==0){
            return null
        }

        return "$imageWidth (W) * $imageHeight (H)"

    }

}
