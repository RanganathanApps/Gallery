package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.FeedbackViewModel
import kotlinx.android.synthetic.main.content_help_feedback.*

class HelpFeedbackActivity : BaseActivity() {

    private lateinit var feedbackViewModel: FeedbackViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_help_feedback)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)
        feedbackViewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)
        btnSubmitFeedback.setOnClickListener {
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
            showToast(feedback)
            progressBarCircular.visibility = GONE

        }.addOnFailureListener {
            showToast("addOnFailureListener")
            progressBarCircular.visibility = GONE

        }
    }


}
