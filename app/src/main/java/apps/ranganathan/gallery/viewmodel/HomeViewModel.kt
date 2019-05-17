package apps.ranganathan.gallery.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import apps.ranganathan.configlibrary.base.BaseAppActivity
import java.io.File


open class HomeViewModel : BaseViewModel(){

    lateinit var uri: Uri

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


    fun getAllImages(context: Context): List<File> {
        val results = mutableListOf<File>()
        results.addAll(getExternalStorageContent(context))
       // results.addAll(getInternalStorageContent(context))
        return results
    }

    fun getAlbums(activity: BaseAppActivity): List<File> {
        return getAlbumFileFromUri(activity,uri)
    }



    fun getAllImagesUnderFolder(context: Context, file: File): List<File> = getImageFileFromUri(context, Uri.fromFile(file))

    private fun getInternalStorageContent(context: Context): Collection<File> = getImageFileFromUri(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

    private fun getExternalStorageContent(context: Context): Collection<File> = getImageFileFromUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    private fun getImageFileFromUri(context: Context, uri: Uri): List<File> {
        val cursor = context.contentResolver.query(uri,projection,
            null, null, "$orderBy DESC")

        val results = mutableListOf<File>()

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
            Log.w("path : ",absolutePathOfImage)
            results.add(File(absolutePathOfImage))
        }

        cursor.close()

        return results
    }

    private fun getAlbumFileFromUri(context: Context, uri: Uri): List<File> {

        val cursor2 = context.contentResolver.query(uri, projection,
            BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)
        val results = mutableListOf<File>()

        val folderName = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val imageData = cursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor2.moveToNext()) {
            val bucket = cursor2.getString(folderName)
            val imagePath = cursor2.getString(imageData)
            Log.w("albums  : ",bucket)
            val selectionArgs = arrayOf("%" + cursor2.getString(folderName) + "%")
            val selection = MediaStore.Video.Media.DATA + " like ? "
            val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            val cursorBucket = context.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)
            Log.w("albums :", "album size :" + cursorBucket.count + " "+imagePath)
            results.add(File(imagePath))

        }
        cursor2.close()

        return results
    }
}