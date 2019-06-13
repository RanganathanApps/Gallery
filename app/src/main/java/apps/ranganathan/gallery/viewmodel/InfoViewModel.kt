package apps.ranganathan.gallery.viewmodel

import android.util.Log
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*




open class InfoViewModel : BaseViewModel() {


    init {

    }

    fun getFilePath(file: File): String {
        return file.absolutePath
    }

    fun getFileDate(file: File): String {
        var dateString: String = ""
        try {
            val date = Date(file.lastModified())
            dateString = date.toString()
            Log.w("Date:", dateString)
            val inputFormat = SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US
            )
            inputFormat.timeZone = TimeZone.getTimeZone("Etc/UTC")
            var spf = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyy")
            val newDate = inputFormat.parse(dateString)
            spf = SimpleDateFormat("dd MMM yyyy \nEEEE hh:mm a")
            dateString = spf.format(newDate)
        } catch (e: Exception) {
            return dateString
        } finally {
            return dateString
        }
    }

    fun getFileDateOnly(date: Date): String {
        var dateString: String = ""
        try {
            dateString = date.toString()
            Log.w("Date:", dateString)
            val inputFormat = SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US
            )
            inputFormat.timeZone = TimeZone.getTimeZone("Etc/UTC")
            var spf = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyy")
            val newDate = inputFormat.parse(dateString)
            spf = SimpleDateFormat("EEE dd MMM yyyy")
            dateString = spf.format(newDate)
        } catch (e: Exception) {
            return dateString
        } finally {
            return dateString
        }
    }

    fun getFileSize(file: File): String {
        val length = file.length()// Size in KB
        val lengthKB = length / 1024 // Size in KB
        return formatFileSize(length)!!//"$lengthKB KB"
    }

    fun formatFileSize(size: Long): String? {
        var hrSize: String? = null

        val b = size.toDouble()
        val k = size / 1024.0
        val m = size / 1024.0 / 1024.0
        val g = size / 1024.0 / 1024.0 / 1024.0
        val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0

        val dec = DecimalFormat("0.00")

        if (t > 1) {
            hrSize = dec.format(t)
            hrSize = "$hrSize TB"
        } else if (g > 1) {
            hrSize = dec.format(g)
            hrSize = "$hrSize GB"
        } else if (m > 1) {
            hrSize = dec.format(m)
            hrSize = "$hrSize MB"
        } else if (k > 1) {
            hrSize = dec.format(k)
            hrSize = "$hrSize KB"
        } else {
            hrSize = dec.format(b)
            hrSize = "$hrSize Bytes"
        }

        return hrSize
    }

}