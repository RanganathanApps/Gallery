package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.InfoViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_app_info.*
import kotlinx.android.synthetic.main.content_info.*
import java.io.File

class AppInfoActivity : BaseActivity() {

    private lateinit var infoViewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_app_info)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)
        txtAppVersionAppInfo.text = "v "+BuildConfig.VERSION_NAME


    }

}
