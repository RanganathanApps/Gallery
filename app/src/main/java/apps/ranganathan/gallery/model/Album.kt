package apps.ranganathan.gallery.model

import java.io.File
import java.io.Serializable
import java.util.*

open class Album : Serializable, Comparable<Album> {
    override fun compareTo(other: Album): Int {
        return 1
    }

    lateinit var name: String
    lateinit var count: String
    lateinit var albumUri: String
    lateinit var path: String
    lateinit var bucket: String
    lateinit var date: Date
    lateinit var dateString: String
    lateinit var file: File
    lateinit var subfiles: ArrayList<File>
    var isSelected: Boolean = false

}