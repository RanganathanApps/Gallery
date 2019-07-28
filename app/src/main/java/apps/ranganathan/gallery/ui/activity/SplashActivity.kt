package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.BR
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.databinding.ActivitySplashBinding
import apps.ranganathan.gallery.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {


    lateinit var mDelayHandler: Handler
    private lateinit var splashViewModel: SplashViewModel

    /*  @Inject
      lateinit var prefs: Prefs*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)


        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        binding.setVariable(BR.splashViewModel, splashViewModel)

        splashViewModel.animateUI(imageSettings)
        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler.postDelayed(mRunnable, 100)


    }


    val mRunnable: Runnable = Runnable {

        startAppActivity(context, HomeActivity::class.java)
        finish()
        /* if (splashViewModel.auth.currentUser != null) {
             if (prefs.getData(PrefMgr.KEY_ACCESS_TOKEN).equals("")) {
                 splashViewModel.auth.signOut()
                 newIntent(this@SplashActivity, LoginActivity::class.java, "")
             } else {
                 newIntent(this@SplashActivity, HomeActivity::class.java, "")
                 finish()
             }
         } else {
             newIntent(this@SplashActivity, LoginActivity::class.java, "")
         }*/


        /* if (!isFinishing) {
             //viewModelSetup(this, splashViewModel)
             splashViewModel.getAppVersion().observe(this@SplashActivity, Observer {
                 if (BuildConfig.VERSION_NAME.equals(it!!.appVersion.version)) {
                     newIntent(this@SplashActivity, AndroidVersionActivity::class.java, "")

                 } else {
                     if (it!!.appVersion.mandatory == "1") {
                         showAlert("Update!",
                             "New Version Available v:${it!!.appVersion.version} \ncurrent v:${BuildConfig.VERSION_NAME}",
                             object : Utils.OnClickListener {
                                 override fun onClick(v: View) {
                                     // Toast.makeText(v.getContext(), "Click", Toast.LENGTH_SHORT).show()
                                     showMsg(txtAppVersion, "Click")
                                 }
                             },
                             object : Utils.OnClickListener {
                                 override fun onClick(v: View) {
                                     if (prefs.getData(KEY_TOKEN) == "") {
                                         newIntent(this@SplashActivity, LoginActivity::class.java, "")
                                     }else{
                                         newIntent(this@SplashActivity, HomeActivity::class.java, "")
                                     }
                                 }
                             }
                         )
                     }
                 }


             })
         }*/
    }

    override fun onBackPressed() {
        parentJob.cancel()
        super.onBackPressed()
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}
