package apps.ranganathan.gallery.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.FeedbackViewModel
import kotlinx.android.synthetic.main.content_rate_app.*

class RateAppActivity : BaseActivity() {

    private lateinit var feedbackViewModel: FeedbackViewModel
    var isConnected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_rate_app)
        setAppBar(getString(R.string.rate_us))
        setToolBarTitle(getString(R.string.rate_us))
        changeToolbarNavIconColor(R.color.colorWhite)


        setConnectivityChange()
        getIsConnected().observe(this, Observer {
            isConnected = it

        })
        feedbackViewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)
        btnRateNow.setOnClickListener {
            if (!isConnected) {
                showToast(getString(R.string.no_internet_connection))
                return@setOnClickListener
            }
            navigateToPlayStore()

        }

    }

    private fun navigateToPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=$packageName"
            )
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }


}
