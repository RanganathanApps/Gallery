package apps.ranganathan.gallery.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.adapter.ViewTypeAdapter
import apps.ranganathan.gallery.model.Album
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.content.res.Resources


open class HomeViewModel : BaseViewModel(){

   companion object{
       lateinit var appContext: Context
       lateinit var allImagesResults: java.util.ArrayList<Album>
       lateinit var albumsResults: java.util.ArrayList<Album>

       fun getResources(): Resources = appContext.resources

      fun getAll(): java.util.ArrayList<Album> {
          if (!::allImagesResults.isInitialized) {
              allImagesResults = ArrayList<Album>()

          }
          return allImagesResults
      }

       fun initAlbums(): java.util.ArrayList<Album> {
           if (!::albumsResults.isInitialized) {
               albumsResults = ArrayList<Album>()

           }
           return albumsResults
       }


   }

    private lateinit var albums: ArrayList<Album>
    val inputFormatSystem = SimpleDateFormat( "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US )
    val outputFormatDateWithDay = SimpleDateFormat("EEE dd MMM yyyy")
    val outputFormatSystemDateOnly = SimpleDateFormat("dd-MM-yyyy")

    lateinit var uri: Uri

    var pathDirectory = StringBuffer()

    val orderBy = MediaStore.Images.Media._ID

    val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
    val BUCKET_ORDER_BY = "MAX(datetaken) DESC"

    private var projection: Array<String>


    private var absolutePathOfImage: String? = null

    private lateinit var listOfAllImages: ArrayList<String>

    init {
       uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

       /*projection = arrayOf(MediaStore.MediaColumns.DATA,
           MediaStore.MediaColumns.DATE_ADDED,
           MediaStore.MediaColumns.DISPLAY_NAME,
           MediaStore.MediaColumns.MIME_TYPE)*/

        projection = arrayOf(MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA)

        getAll()
        initAlbums()

   }

    fun setTypes(context: Context): ViewTypeAdapter {
        val types = arrayOf<String>("Albums","Photos")
        val adapter = ViewTypeAdapter(context,types)
       return adapter
    }

    fun getAllImages(context: Context): ArrayList<Album> {
        if (allImagesResults.size==0 ) {
            allImagesResults = ArrayList<Album>()
            allImagesResults.addAll(getExternalStorageContent(context))
            //results.addAll(getInternalStorageContent(context))
        }

        return allImagesResults
    }





    fun getAlbums(context: Context): List<Album> {
        if (albumsResults.size==0 ) {
            albumsResults = ArrayList<Album>()
            albumsResults.addAll(getAlbumFileFromUri(context,uri))
            //results.addAll(getInternalStorageContent(context))
        }
        return albumsResults
    }

    fun getSpecificDateImages(context: Context,album: Album): MutableList<Album> {

        val results = mutableListOf<Album>()
        val k = getImageFileFromDate(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,getFileDateOnlyString(album.date,inputFormatSystem,outputFormatSystemDateOnly),album)
        results.addAll(k)
        //results.addAll(getInternalStorageContent(context))
        return results
    }

  fun getSpecificFolderImages(context: Context,album: Album): MutableList<Album> {

               val results = mutableListOf<Album>()
        val k = getImageFileFromFolder(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,getFileDateOnlyString(album.date,inputFormatSystem,outputFormatSystemDateOnly),album)
        results.addAll(k)
        // results.addAll(getInternalStorageContent(context))
        return results
    }

    public fun setMediaMounted(context: Context, file: File){
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.path),
            arrayOf("image/jpeg"), null
        )
        var scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        var contentUri = Uri.fromFile(file)
        scanIntent.data = contentUri
        context.sendBroadcast(scanIntent)

    }





    private fun getExternalStorageContent(context: Context): Collection<Album> = getImageFileFromUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    private fun getInternalStorageContent(context: Context): Collection<Album> = getImageFileFromUri(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI)


    private fun getImageFileFromUri(context: Context, uri: Uri): List<Album> {
        val albums = mutableListOf<Album>()
        try {
            var  album =Album()
            var file : File
            var currentDate = Date()
            var lastModified = Date()
            val cursor = context.contentResolver.query(uri,projection,
                null, null, "$orderBy DESC")
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                Log.w("path actual:", ""+absolutePathOfImage)
                val folderName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val bucket = cursor.getString(folderName)

                if (absolutePathOfImage!!.contains("/mnt/media_rw/")){
                    absolutePathOfImage = absolutePathOfImage!!.replace("/mnt/media_rw/","/storage/")
                }

                val filePathUri = Uri.parse(absolutePathOfImage)
                val size = filePathUri.pathSegments.size -1
                pathDirectory = StringBuffer()
                for (i in 0 until size){
                    pathDirectory.append("/")
                    pathDirectory .append(filePathUri.pathSegments.get(i))


                }

                Log.w("path formed:", ""+pathDirectory.toString())

                file = File(absolutePathOfImage)
                lastModified = getFileDateOnly(file)

                album = Album()
                album.count = ""
                album.date = lastModified
                album.dateString = getFormattedDate(toCalendar(lastModified))
                album.name = File(absolutePathOfImage).nameWithoutExtension
                album.file = File(absolutePathOfImage)
                album.bucket = bucket
                album.path = pathDirectory.toString()
                album.albumUri = Uri.fromFile(File(absolutePathOfImage)).toString()
                albums.add(album)
            }

            cursor.close()

            return albums
        } catch (e: Exception) {
            return albums
        }
    }

    private fun getImageFileFromDate(context: Context, uri: Uri,date:String,albumfile: Album): List<Album> {
        val albums = mutableListOf<Album>()
        try {
            var  album =Album()
            var file : File
            var currentDate = Date()
            var lastModified = Date()

            val cursor = context.contentResolver.query(uri,projection,
                null,null, "$orderBy DESC"
            )
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                file = File(absolutePathOfImage)
                lastModified = getFileDateOnly(file)

                if (getFileDateOnlyString(albumfile.date,inputFormatSystem,outputFormatSystemDateOnly).equals(getFileDateOnlyString(lastModified,inputFormatSystem,outputFormatSystemDateOnly))) {
                    album = Album()
                    album.count = ""
                    album.date = lastModified
                    album.dateString = getFormattedDate(toCalendar(lastModified))//getFileDateOnlyString(lastModified, inputFormatSystem, outputFormatDateWithDay)
                    album.name = File(absolutePathOfImage).nameWithoutExtension
                    album.file = File(absolutePathOfImage)
                    album.albumUri = Uri.fromFile(File(absolutePathOfImage)).toString()
                    albums.add(album)
                }
            }

            cursor.close()

            return albums
        } catch (e: Exception) {
            return albums
        }
    }

    private fun getImageFileFromFolder(context: Context, uri: Uri,date:String,albumfile: Album): List<Album> {
        val albums = mutableListOf<Album>()
        try {
            var  album =Album()
            var file : File
            var currentDate = Date()
            var lastFolder: String

            val cursor = context.contentResolver.query(uri,projection,
                null,null, "$orderBy DESC"
            )
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                file = File(absolutePathOfImage)
                lastFolder = file.parentFile.name

                if (albumfile.bucket.equals(lastFolder)) {
                    album = Album()
                    album.count = ""
                    album.date = Date(file.lastModified())
                    album.dateString = getFormattedDate(toCalendar(Date(file.lastModified())))//getFileDateOnlyString(lastModified, inputFormatSystem, outputFormatDateWithDay)
                    album.name = File(absolutePathOfImage).nameWithoutExtension
                    album.file = File(absolutePathOfImage)
                    album.albumUri = Uri.fromFile(File(absolutePathOfImage)).toString()
                    albums.add(album)
                }
            }

            cursor.close()

            return albums
        } catch (e: Exception) {
            return albums
        }
    }

    fun toCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    fun getFileDateOnlyString(date: Date,input:SimpleDateFormat,output:SimpleDateFormat): String {
        var dateString: String = ""
        try {
            dateString = date.toString()
            //Log.w("Date:", dateString)
            input.timeZone = TimeZone.getTimeZone("Etc/UTC")
            val newDate = input.parse(dateString)
            dateString = output.format(newDate)
        } catch (e: Exception) {
            return dateString
        } finally {
            return dateString
        }
    }
    fun getFormattedDate(calendar: Calendar): String {


        val now = Calendar.getInstance()

        val timeFormatString = "h:mm aa"
        val dateTimeFormatString = "EEEE, MMMM d, h:mm aa"
        val HOURS = (60 * 60 * 60).toLong()
        return if (now.get(Calendar.DATE) === calendar.get(Calendar.DATE)) {
            "Today "
        } else if (now.get(Calendar.DATE) - calendar.get(Calendar.DATE) === 1) {
            "Yesterday "
        } else if (now.get(Calendar.YEAR) === calendar.get(Calendar.YEAR)) {
            getFileDateOnlyString(calendar.time,inputFormatSystem,outputFormatDateWithDay)
        } else {
            getFileDateOnlyString(calendar.time,inputFormatSystem,outputFormatDateWithDay)
        }
    }
    fun getFileDateOnly(file: File): Date {
        var date = Date(file.lastModified())
        var clearDate = Date()
        try {
            val millisInDay = (60 * 60 * 24 * 1000).toLong()
            val currentTime = date.time
            val dateOnly = currentTime / millisInDay * millisInDay
            clearDate = Date(dateOnly)

            val rightNow = Calendar.getInstance()
            rightNow.timeInMillis = file.lastModified()
            date = rightNow.time
        } catch (e: Exception) {
            return clearDate
        } finally {
            return clearDate
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
            var imagePath = cursor2.getString(imageData)
            Log.w("bucket  : ",bucket)
            val selectionArgs = arrayOf("%$bucket%")
            val selection = MediaStore.Video.Media.DATA + " like ? "
            val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            val cursorBucket = context.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)
           // Log.w("albums :", "size :" + cursorBucket.count + ", path "+imagePath)

            if (imagePath!!.contains("/mnt/media_rw/")){
                imagePath = imagePath!!.replace("/mnt/media_rw/","/storage/")
            }

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
            album.file = File(imagePath)
            album.path = pathDirectory.toString()
            album.count = ""+imageReader(getDirectory(album.path))!!.size
            album.name = bucket
            album.albumUri = Uri.fromFile(File(imagePath)).toString()

            albums.add(album)

        }
        cursor2.close()

        return albums
    }

    fun imageReader(root : File): ArrayList<File>? {
        val a = ArrayList<File>()
        val files = root.listFiles()

        try {
            files.sortWith(Comparator { file1, file2 ->
                val k = file1.lastModified() - file2.lastModified()
                if (k < 0) {
                    1
                } else if (k == 0L) {
                    0
                } else {
                    -1
                }
            })

            if (files!=null) {
                for (i in 0 until files.size) {
                    if (files[i].name.endsWith(".jpg")
                        || files[i].name.endsWith(".png")
                        || files[i].name.endsWith(".gif")
                        || files[i].name.endsWith(".bmp")
                        || files[i].name.endsWith(".WebP")
                        || files[i].name.endsWith(".jpeg")
                        || files[i].name.endsWith(".JPG")
                        || files[i].name.endsWith(".PNG")
                        || files[i].name.endsWith(".GIF")
                        || files[i].name.endsWith(".BMP")
                        || files[i].name.endsWith(".WEBP")
                        || files[i].name.endsWith(".JPEG")) {
                        a.add(files[i])
                    }
                }
            }
        } catch (e: Exception) {

        }


        return a
    }

    fun getDirectory(folderName:String) :File{
        var externalStorageAbsolutePath: String = Environment.getExternalStorageDirectory()!!.absolutePath
        //Log.w("albums folderName", "$folderName  $externalStorageAbsolutePath")
        if (!folderName.contains("storage")){
            return File( externalStorageAbsolutePath+folderName)
        }
        val file = File( folderName)
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

    fun makeHideShow(recyclerView : RecyclerView,navigation:BottomNavigationView){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && navigation.isShown) {
                    //navigation.visibility = View.GONE
                    slideDown(navigation)
                    //hideBottomNavigationView(navigation)
                } else if (dy < 0) {
                    //navigation.visibility = View.VISIBLE
                    slideUp(navigation)
                    //showBottomNavigationView(navigation)

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f, // fromXDelta
            0f, // toXDelta
            view.height.toFloat(), // fromYDelta
            0f
        )                // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        view.visibility = View.GONE
        val animate = TranslateAnimation(
            0f, // fromXDelta
            0f, // toXDelta
            0f, // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun saveImage(context: Context,myBitmap: Bitmap,directory:String): String {


        val bytes = ByteArrayOutputStream()

        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + directory
        )
        // have the object build the directory structure, if needed.
        //Log.w("save image", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            //Log.w("save", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            myBitmap.compress(Bitmap.CompressFormat.PNG,  100,  fo)
            fo.flush()
            //fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                context,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
           // Log.w("save ", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    fun getImages(files:ArrayList<File>): ArrayList<Album> {
        albums = ArrayList<Album>()
        var album = Album()
        for (file in files) {
            album = Album()
            album.name = file.nameWithoutExtension
            album.file = file
            album.bucket = file.parentFile.name
            album.date = Date(file.lastModified())
            album.albumUri = Uri.fromFile(file).toString()
            albums.add(album)
        }
        return albums
    }

    fun delete(context: Context, file: File): Boolean {
        val where = MediaStore.MediaColumns.DATA + "=?"
        val selectionArgs = arrayOf(file.absolutePath)
        val contentResolver = context.contentResolver
        var filesUri = MediaStore.Files.getContentUri("external")

        contentResolver.delete(filesUri, where, selectionArgs)

        if (file.exists()) {
            //file.delete()
            filesUri = MediaStore.Files.getContentUri("internal")
            contentResolver.delete(filesUri, where, selectionArgs)
        }
        return !file.exists()
    }
}