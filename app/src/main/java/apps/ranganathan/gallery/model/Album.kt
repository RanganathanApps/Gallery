package apps.ranganathan.gallery.model

import java.io.File
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

open class Album : Serializable{
    lateinit var name:String
    lateinit var count:String
    lateinit var albumUri:String
    lateinit var path:String
    lateinit var date: Date
    lateinit var dateString: String
    lateinit var file:File
    lateinit var subfiles: ArrayList<File>

}