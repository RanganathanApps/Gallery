package apps.ranganathan.gallery.app

import android.app.Application
import android.content.Context
import apps.ranganathan.gallery.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ViewPump.init(ViewPump.builder()
            .addInterceptor( CalligraphyInterceptor(
                     CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Hind-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build())
            ).build())
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


}