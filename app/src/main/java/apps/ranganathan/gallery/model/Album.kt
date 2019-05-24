package apps.ranganathan.gallery.model

import java.io.File
import java.io.Serializable

open class Album : Serializable{
    lateinit var name:String
    lateinit var count:String
    lateinit var albumUri:String
    lateinit var path:String
    lateinit var file:File

}