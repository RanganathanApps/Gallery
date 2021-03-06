package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import android.view.View.*
import apps.ranganathan.configlibrary.utils.ForceUpdateChecker
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.InfoViewModel
import kotlinx.android.synthetic.main.content_app_info.*





class AppInfoActivity : BaseActivity() ,ForceUpdateChecker.OnUpdateNeededListener{

    private lateinit var infoViewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_app_info)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)
        txtAppVersionAppInfo.text = "v " + BuildConfig.VERSION_NAME

        setConnectivityChange()
        btnCheckForUpdate.setOnClickListener {
            if (!getIsConnected().value!!){
                showToast(getString(R.string.no_internet_connection))
                return@setOnClickListener
            }
            txtAppVersionAppInfo.visibility = INVISIBLE
            progressBarCircular.visibility = VISIBLE
            ForceUpdateChecker.with(this@AppInfoActivity).onUpdateNeeded(this@AppInfoActivity).check()
        }




    }

    override fun onUpToDate() {
        txtAppVersionAppInfo.visibility = VISIBLE
        runOnUiThread {  progressBarCircular.visibility = INVISIBLE }
        super.onUpToDate()
    }

    override fun onUpdateNeeded(updateUrl: String) {
        txtAppVersionAppInfo.visibility = VISIBLE
        runOnUiThread {  progressBarCircular.visibility = INVISIBLE }
        super.onUpdateNeeded(updateUrl)
    }


}
