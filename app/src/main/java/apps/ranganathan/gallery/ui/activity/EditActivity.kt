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
import ja.burhanrashid52.photoeditor.PhotoEditor
import android.graphics.Typeface
import android.view.View.GONE
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.android.synthetic.main.activity_picture_view.*
import kotlinx.android.synthetic.main.content_editting.*
import kotlinx.android.synthetic.main.toolbar_home.*


class EditActivity : BaseActivity() {

    private var mPhotoEditor: PhotoEditor? = null
    private lateinit var infoViewModel: InfoViewModel

    private lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editing)
        setAppBar("")
        appBar.visibility = GONE
        changeToolbarNavIconColor(R.color.colorWhite)

        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel::class.java)

        //Use custom font using latest support library
        //val mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium)

//loading font from assest
        //val mEmojiTypeFace = Typeface.createFromAsset(assets, "emojione-android.ttf")

        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true)
            .build()
        mPhotoEditor!!.setFilterEffect(PhotoFilter.BRIGHTNESS);
        mPhotoEditor!!.setBrushDrawingMode(true)




    }

}
