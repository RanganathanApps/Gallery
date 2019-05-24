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


open class HomeViewModel : BaseViewModel(){

    lateinit var uri: Uri

    var pathDirectory = StringBuffer()

    val orderBy = MediaStore.Images.Media._ID

    val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
    val BUCKET_ORDER_BY = "MAX(datetaken) DESC"

    private var projection: Array<String>

    private var absolutePathOfImage: String? = null

    private lateinit var listOfAllImages: ArrayList<String>

    init {
       uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

       /*projection = arrayOf(MediaStore.MediaColumns.DATA,
           MediaStore.MediaColumns.DATE_ADDED,
           MediaStore.MediaColumns.DISPLAY_NAME,
           MediaStore.MediaColumns.MIME_TYPE)*/

        projection = arrayOf(MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA)



   }

    fun setTypes(context: Context): ViewTypeAdapter {
        val types = arrayOf<String>("Albums","Photos")
        val adapter = ViewTypeAdapter(context,types)
       return adapter
    }

    fun getAllImages(context: Context): List<Album> {
        val results = mutableListOf<Album>()
        results.addAll(getExternalStorageContent(context))
       // results.addAll(getInternalStorageContent(context))
        return results
    }

    fun getAlbums(activity: BaseAppActivity): List<Album> {
        return getAlbumFileFromUri(activity,uri)
    }



    fun getAllImagesUnderFolder(context: Context, file: File): List<Album> = getImageFileFromUri(context, Uri.fromFile(file))

    private fun getInternalStorageContent(context: Context): Collection<Album> = getImageFileFromUri(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

    private fun getExternalStorageContent(context: Context): Collection<Album> = getImageFileFromUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    private fun getImageFileFromUri(context: Context, uri: Uri): List<Album> {
        val albums = mutableListOf<Album>()
        try {
            var  album =Album()

            val cursor = context.contentResolver.query(uri,projection,
                null, null, "$orderBy DESC")



            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                Log.w("path : ",absolutePathOfImage)
                //albums.add(File(absolutePathOfImage))
                album = Album()
                album.count = ""
                album.name = File(absolutePathOfImage).nameWithoutExtension
                album.albumUri = Uri.fromFile(File(absolutePathOfImage)).toString()
                albums.add(album)
            }

            cursor.close()

            return albums
        } catch (e: Exception) {
            return albums
        }
    }

    private fun getAlbumFileFromUri(context: Context, uri: Uri): List<Album> {

        var  album =Album()
        val cursor2 = context.contentResolver.query(uri, projection,
            BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)
        val albums = mutableListOf<Album>()

        val folderName = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val imageData = cursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor2.moveToNext()) {
            val bucket = cursor2.getString(folderName)
            val imagePath = cursor2.getString(imageData)
            Log.w("albums  : ",bucket)
            val selectionArgs = arrayOf("%$bucket%")
            val selection = MediaStore.Video.Media.DATA + " like ? "
            val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            val cursorBucket = context.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)
            Log.w("albums :", "size :" + cursorBucket.count + ", path "+imagePath)
            val filePathUri = Uri.parse(imagePath)
            val fileName = filePathUri.getLastPathSegment().toString()
            val size = filePathUri.pathSegments.size -1
            pathDirectory = StringBuffer()
            for (i in 0 until size){
                pathDirectory.append("/")
                pathDirectory .append(filePathUri.pathSegments.get(i))

            }
            Log.w("albums :", "filePath fileName  :$pathDirectory")




            album = Album()
            album.count = ""+cursorBucket.count
            album.file = File(imagePath)
            album.name = bucket
            album.path = pathDirectory.toString()
            album.albumUri = Uri.fromFile(File(imagePath)).toString()

            albums.add(album)

        }
        cursor2.close()

        return albums
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

    fun getDirectory(folderName:String) :File{
        var externalStorageAbsolutePath: String = Environment.getExternalStorageDirectory()!!.absolutePath
        Log.w("albums AbsolutePath", " " + externalStorageAbsolutePath)
        val file = File(externalStorageAbsolutePath + File.separator + folderName)
        return  file

    }

    fun getImagesInFile(file: File): ArrayList<File>? {
        Log.w("albums fullpath", "" + file)
        val images = imageReader(file)
        if (images!=null){
            for (a in images){
                Log.w("albums absolutePath", "" + a.absolutePath)
            }
        }
        return images
    }
}