package apps.ranganathan.gallery.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.BaseSectionAdapter
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity

abstract class BaseViewHolder : RecyclerView.ViewHolder, ListAdapter.Binder<Any> {

    var activity: BaseActivity? = null
    internal lateinit var adapter: ListAdapter
    internal var clickable: Clickable? = null

    internal fun setActivity(activity : BaseActivity, adapter: ListAdapter, clickable: Clickable){
        this.activity = activity
        this.adapter = adapter
        this.clickable = clickable
    }

    constructor(itemView: View) : super(itemView)

    override fun clicked(adapter: ListAdapter, index: Int) {
        clickable!!.clicked(adapter,index)
    }
    override fun onLongClicked(adapter: ListAdapter, index: Int) {
        clickable!!.onLongClicked(adapter,index)
    }

    /*interface for data binding*/
    internal interface Clickable{
        fun clicked(adapter:ListAdapter,index:Int)
        fun onLongClicked(adapter:ListAdapter,index:Int)
    }

}