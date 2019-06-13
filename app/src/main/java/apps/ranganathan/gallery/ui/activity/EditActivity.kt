package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import android.view.View.GONE
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.InfoViewModel
import kotlinx.android.synthetic.main.activity_picture_view.*


class EditActivity : BaseActivity() {

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

    }

}
