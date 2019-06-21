package apps.ranganathan.gallery.ui.fragment

import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel;
import apps.ranganathan.configlibrary.base.BaseAppActivity.Companion.makeLog
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraViewModel : HomeViewModel() {

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String,DIRECTORY:String): File {
        val timeStamp = SimpleDateFormat("MMdd_HHmm").format(Date())
        val imageFileName = "Capture_$fileName $timeStamp.jpg"
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString() + DIRECTORY)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            makeLog("failed to create directory")
        }

        // Return the file target for the photo based on filename

        val path = mediaStorageDir.path + File.separator + imageFileName
        return File(path)
    }
}
