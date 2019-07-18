package apps.ranganathan.gallery.viewmodel

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import apps.ranganathan.gallery.viewholders.HeaderViewHolder
import kotlinx.android.synthetic.main.photos_fragment.*
import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import apps.ranganathan.gallery.model.ParentModel


class PhotosViewModel : HomeViewModel() {

    private lateinit var adapter: ListAdapter

    internal fun setDataToAdapter(activity: BaseActivity, layoutManager: LayoutManager,files: ArrayList<Any>): ListAdapter {

        adapter = object : ListAdapter() {

            override fun getLayoutId(position: Int, obj: Any): Int {
                return when ((obj as Album).isSectionHeader) {
                    true -> {
                        R.layout.item_header_photos
                    }
                    else -> {
                        R.layout.item_photos
                    }
                }
                /*return when (obj) {
                    is ParentModel -> {
                        R.layout.item_header_photos
                    }
                    else -> {
                        R.layout.item_photos
                    }
                }*/
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
                                    val anotherMap = mapOf(
                                        "tag" to "date",
                                        "date" to album.dateString, "position" to index, "count" to album.count,
                                        "album" to album
                                    )
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

                        val hol=  HeaderViewHolder(view)
                        hol.setActivity(activity as BaseActivity,adapter = adapter, clickable = object : BaseViewHolder.Clickable {

                            override fun clicked(adapter: ListAdapter, index: Int) {

                            }

                            override fun onLongClicked(adapter: ListAdapter, index: Int) {

                            }

                        })

                        return hol
                    }
                }
            }
        }


        adapter.setItems(files)
        //activity.recyclerPhotos.layoutManager =  layoutManager
        activity.recyclerPhotos.hasFixedSize()
        activity.recyclerPhotos.adapter = adapter

        val glm = GridLayoutManager(activity, 3)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (adapter.getItemViewType(position)) {
                    R.layout.item_header_photos -> return 3
                    else -> return 1
                }
            }
        }
        activity.recyclerPhotos.layoutManager = glm
        return adapter
    }

}