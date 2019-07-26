package apps.ranganathan.gallery.viewmodel

import androidx.lifecycle.MutableLiveData


open class PictureViewModel : HomeViewModel() {

    open var position = MutableLiveData<Int>()


    init {


    }


}