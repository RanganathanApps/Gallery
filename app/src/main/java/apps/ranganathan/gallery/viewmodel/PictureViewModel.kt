package apps.ranganathan.gallery.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.adapter.ViewTypeAdapter
import apps.ranganathan.gallery.model.Album
import kotlinx.android.synthetic.main.toolbar_home.*
import java.io.File


open class PictureViewModel : BaseViewModel(){



    init {


   }



    fun imageReader(root : File): ArrayList<File>? {
        val a = ArrayList<File>()
        val files = root.listFiles()
        if (files!=null) {
            for (i in 0..files.size - 1) {
                if (files[i].name.endsWith(".jpg")) {
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