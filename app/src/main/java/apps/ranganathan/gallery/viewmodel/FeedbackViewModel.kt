package apps.ranganathan.gallery.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


open class FeedbackViewModel : HomeViewModel() {

    open var position = MutableLiveData<Int>()

    lateinit var db: DocumentReference


    init {
        db = FirebaseFirestore.getInstance().document("")

    }


}