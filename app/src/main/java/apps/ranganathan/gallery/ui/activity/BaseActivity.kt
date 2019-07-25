package apps.ranganathan.gallery.ui.activity

import android.content.Context
import apps.ranganathan.configlibrary.base.BaseAppActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

open class BaseActivity : BaseAppActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    private fun initCode() {

    }




}
