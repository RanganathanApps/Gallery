package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import android.view.View.VISIBLE
import apps.ranganathan.configlibrary.utils.ForceUpdateChecker
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.InfoViewModel
import kotlinx.android.synthetic.main.content_help_feedback.*

class HelpFeedbackActivity : BaseActivity() {

    private lateinit var infoViewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_help_feedback)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)
        btnSubmitFeedback.setOnClickListener {
            if (!txtFeedbackEmail.text.isNullOrEmpty()){

                updateToFirebase(txtFeedbackEmail.text.toString())
            }else{
                showToast(getString(R.string.email_mandatory))
            }
        }

    }

    private fun updateToFirebase(feedback: String) {
        progressBarCircular.visibility = VISIBLE
        showToast(feedback)
    }


}
