package apps.ranganathan.gallery.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.MutableInt
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.adapter.ViewTypeAdapter
import apps.ranganathan.gallery.model.Album
import kotlinx.android.synthetic.main.toolbar_home.*
import java.io.File
import java.lang.Long.compare
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


open class PictureViewModel : HomeViewModel(){

    open  var position = MutableLiveData<Int>()


    init {


   }







    fun delete(context: Context, file: File): Boolean {
        val where = MediaStore.MediaColumns.DATA + "=?"
        val selectionArgs = arrayOf(file.absolutePath)
        val contentResolver = context.contentResolver
        val filesUri = MediaStore.Files.getContentUri("external")

        contentResolver.delete(filesUri, where, selectionArgs)

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs)
        }
        return !file.exists()
    }

}