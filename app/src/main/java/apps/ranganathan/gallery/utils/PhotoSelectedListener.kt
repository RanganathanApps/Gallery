package apps.ranganathan.gallery.utils

import apps.ranganathan.gallery.model.Album

interface PhotoSelectedListener {

    fun onPhotoSelected(position:Int,list:List<Album>)
    fun onItemSelected(position:Int,list:List<Album>)
}