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


open class PictureViewModel : BaseViewModel(){

    open  var position = MutableLiveData<Int>()


    init {


   }



    fun imageReader(root : File): ArrayList<File>? {
        val a = ArrayList<File>()
        val files = root.listFiles()
        Arrays.sort(files)
       /* Arrays.sort(files, object : Comparator() {
            fun compare(o1: Any, o2: Any): Int {

                return if ((o1 as File).lastModified() > (o2 as File).lastModified()) {
                    -1
                } else if (o1.lastModified() < o2.lastModified()) {
                    +1
                } else {
                    0
                }
            }

        })*/
        //Arrays.sort(files) { f1, f2 -> compare(f1.lastModified(), f2.lastModified()) }
        if (files!=null) {
            for (i in 0..files.size - 1) {
                if (files[i].name.endsWith(".jpg")
                    || files[i].name.endsWith(".png")
                    || files[i].name.endsWith(".gif")
                    || files[i].name.endsWith(".bmp")
                    || files[i].name.endsWith(".WebP")
                    || files[i].name.endsWith(".jpeg") ) {
                    a.add(files[i])
                }
            }
        }
        return a
    }

    fun getDirectory(path:String) :File{
        val file = File(path)
        return  file

    }

    private lateinit var albums: MutableList<Album>

    fun getImages(files:ArrayList<File>): MutableList<Album> {
        albums = arrayListOf()
        var album = Album()
        for (file in files) {
            album = Album()
            album.name = file.nameWithoutExtension
            album.file = file
            album.albumUri = Uri.fromFile(file).toString()
            albums.add(album)
        }
        return albums
    }

    fun getImagesInFile(file: File): ArrayList<File> {
        Log.w("albums file", "" + file)
        val images = imageReader(file)
        if (images!=null){
            for (a in images){
                Log.w("albums absolutePath", "" + a.absolutePath)
            }
        }
        return images!!
    }
}