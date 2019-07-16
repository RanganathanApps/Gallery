package apps.ranganathan.gallery.ui.fragment

import android.view.View
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.ui.activity.RecyclerListActivity
import apps.ranganathan.gallery.viewholders.AlbumViewHolder
import apps.ranganathan.gallery.viewholders.BaseViewHolder
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.photos_fragment.*
import java.util.ArrayList

class PhotosViewModel : HomeViewModel() {

    private lateinit var adapter: ListAdapter

    internal fun setDataToAdapter(activity: BaseActivity, files: ArrayList<Album>) {

        adapter = object : ListAdapter() {

            override fun getLayoutId(position: Int, obj: Any): Int {
                return when (obj) {
                    is Album -> {
                        R.layout.item_photos
                    }
                    else -> {
                        R.layout.item_album
                    }
                }
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                when (viewType) {
                    R.layout.item_photos -> {
                        val hol=  AlbumViewHolder(view)
                        hol.setActivity(activity as BaseActivity,adapter = adapter, clickable = object : BaseViewHolder.Clickable {

                            override fun clicked(adapter: ListAdapter, index: Int) {
                                var album =  adapter.listItems[index] as Album
                                if (adapter.isSelection) {
                                    album.isSelected = !album.isSelected
                                    adapter.notifyItemChanged(index)
                                    (activity as HomeActivity).makeShareaDeleteToolbar(adapter, null, adapter.listItems as List<Album>)
                                } else {
                                    val anotherMap = mapOf("position" to index, "tag" to "photos")
                                    (activity as BaseActivity).startActivityputExtra(
                                        activity as BaseActivity,
                                        PictureViewActivity::class.java,
                                        anotherMap
                                    )
                                }
                            }

                            override fun onLongClicked(adapter: ListAdapter, index: Int) {
                                var album =  adapter.listItems[index] as Album
                                if (!adapter.isSelection){
                                    adapter.isSelection = true
                                    adapter.notifyDataSetChanged()
                                }
                                adapter.isSelection = true
                                album.isSelected = true
                                adapter.notifyItemChanged(index)
                                (activity as HomeActivity).makeShareaDeleteToolbar(adapter, null, adapter.listItems as List<Album>)
                            }

                        })

                        return hol
                    }
                    else -> {
                        return RecyclerListActivity.MovieViewHolder(view)
                    }
                }
            }
        }

        adapter.setItems(files)
        activity.recyclerPhotos.layoutManager = GridLayoutManager(activity, 4) as RecyclerView.LayoutManager?
        activity.recyclerPhotos.hasFixedSize()
        activity.recyclerPhotos.adapter = adapter
    }

}
