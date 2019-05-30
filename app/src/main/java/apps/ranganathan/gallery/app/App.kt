package apps.ranganathan.gallery.app

import android.app.Application

import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class App : Application() {



    override fun onCreate() {
        super.onCreate()

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Poppins-Medium.ttf")
                .setFontAttrId(apps.ranganathan.gallery.R.attr.fontPath).build()
        )
    }


}