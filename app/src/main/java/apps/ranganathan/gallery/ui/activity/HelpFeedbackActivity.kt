package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import apps.ranganathan.configlibrary.utils.ForceUpdateChecker
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.InfoViewModel
import kotlinx.android.synthetic.main.content_app_info.*

class HelpFeedbackActivity : BaseActivity() {

    private lateinit var infoViewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_help_feedback)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)






    }


}
