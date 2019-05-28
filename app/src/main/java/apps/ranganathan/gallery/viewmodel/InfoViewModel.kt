package apps.ranganathan.gallery.viewmodel

import java.io.File
import java.util.*


open class InfoViewModel : BaseViewModel() {


    init {

    }

    fun getFilePath(file: File): String {
        return file.absolutePath
    }

    fun getFileDate(file: File): String {
        return Date(file.lastModified()).toString()
    }

    fun getFileSize(file: File): String {
        val length = file.length()// Size in KB
        val lengthKB = length / 1024 // Size in KB
        return lengthKB.toString()
    }

}