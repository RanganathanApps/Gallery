package apps.ranganathan.gallery.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.configlibrary.utils.Utils
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.FeedbackViewModel
import kotlinx.android.synthetic.main.content_help_feedback.*

class HelpFeedbackActivity : BaseActivity() {

    private lateinit var feedbackViewModel: FeedbackViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_help_feedback)
        setAppBar(getString(R.string.feedback))
        setToolBarTitle(getString(R.string.feedback))
        changeToolbarNavIconColor(R.color.colorWhite)
        setConnectivityChange()
        feedbackViewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)
        btnSubmitFeedback.setOnClickListener {
            if (!getIsConnected().value!!){
                showToast(getString(R.string.no_internet_connection))

                return@setOnClickListener
            }
            if (!txtFeedbackEmail.text.isNullOrEmpty() ){
                if (!txtFeedback.text.isNullOrEmpty()){

                   updateToFirebase(txtFeedbackEmail.text.toString(),txtFeedback.text.toString())
                }else{
                    showToast(getString(R.string.feebcak_cannot_be_empty))
                }
            }else{
                showToast(getString(R.string.email_mandatory))
            }
        }

    }

    private fun updateToFirebase(email: String, feedback: String) {
        progressBarCircular.visibility = VISIBLE

        val items = HashMap<String, Any>()
        items.put("comment", feedback)
        items.put("email", email)

        feedbackViewModel.db.collection("feedbacks").document(email).set(items).addOnSuccessListener {
            showAlert("Great!",getString(R.string.feedback_submitted), object : Utils.OnClickListener {
                override fun onClick(v: View) {
                    finish()
                }

            })
            progressBarCircular.visibility = GONE

        }.addOnFailureListener {
            showToast(getString(R.string.please_try_agin))
            progressBarCircular.visibility = GONE

        }
    }


}
