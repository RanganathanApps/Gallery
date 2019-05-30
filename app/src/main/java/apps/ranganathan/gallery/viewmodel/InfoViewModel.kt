package apps.ranganathan.gallery.viewmodel

import java.io.File
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
            var spf = SimpleDateFormat("E MMM dd, hh:mm:ss")
            val newDate = spf.parse(dateString)
            spf = SimpleDateFormat("dd MMM yyyy")
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
        return "$lengthKB KB"
    }

}