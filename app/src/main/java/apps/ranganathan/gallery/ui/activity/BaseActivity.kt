package apps.ranganathan.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import kotlinx.android.synthetic.main.toolbar.*
import java.io.Serializable

open class BaseActivity : BaseAppActivity() {

    val bundle = Bundle()
    fun setAppBar(title: String) {
        try {
            toolbar.title = title
            toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_black_24dp)
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        } catch (e: Exception) {
            showToast(e.localizedMessage)
        }
    }

    open fun startActivityputExtra(mCon: Context, cls: Class<*>,map: Map<String, Any>) {
        try {
            val intent = Intent(mCon, cls)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                    Intent.FLAG_ACTIVITY_NEW_TASK


            bundle.clear()
            for (pair in map){
                bundle.putSerializable(pair.key, pair.value as Serializable)

            }
            intent.putExtras(bundle)
            mCon.startActivity(intent)
        } catch (e: Exception) {
            showToast(e.message.toString())
        }

    }
}
