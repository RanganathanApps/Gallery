package apps.ranganathan.gallery.viewmodel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.viewholders.AlbumViewHolder
import apps.ranganathan.gallery.viewholders.AlbumsDirectoryViewHolder
import apps.ranganathan.gallery.viewholders.BaseViewHolder
import kotlinx.android.synthetic.main.photos_fragment.*
import java.io.Serializable
import java.util.ArrayList


class AlbumsDirectoryViewModel : HomeViewModel() {

    private lateinit var adapter: ListAdapter

    internal fun setDataToAdapter(
        activity: BaseActivity,
        layoutManager: LayoutManager,
        progressCircularAccent: View,
        files: ArrayList<Any>
    ): ListAdapter {

        adapter = object : ListAdapter() {

            override fun getLayoutId(position: Int, obj: Any): Int {
                val album = obj as Album
                return when (album.isSectionHeader) {
                    true -> {
                        try{
                            if (album.bucket.isNotEmpty()) {
                                R.layout.item_header_album_directory
                            } else
                                R.layout.item_header_photos
                        }catch (e:UninitializedPropertyAccessException){
                            R.layout.item_header_photos
                        }
                    }
                    false -> {
                        R.layout.item_photos
                    }

                }

            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                when (viewType) {
                    R.layout.item_photos -> {
                        val hol = AlbumViewHolder(view)
                        hol.setActivity(
                            activity ,
                            adapter = adapter,
                            clickable = object : BaseViewHolder.Clickable {

                                override fun clicked(adapter: ListAdapter, index: Int) {
                                    var album = adapter.listItems[index] as Album
                                    if (adapter.isSelection) {
                                        album.isSelected = !album.isSelected
                                        adapter.notifyItemChanged(index)
                                        (activity as HomeActivity).makeShareDeleteToolbar(adapter.listItems as List<Album>)
                                    } else {
                                        val anotherMap = mapOf("tag" to "albums_list","directory_ui" to album.bucket,"position" to index,"count" to album.count,"album" to album)

                                        val bundle = Bundle()
                                        for (pair in anotherMap) {
                                            bundle.putSerializable(pair.key, pair.value as Serializable)
                                        }
                                        var intent = Intent(activity,PictureViewActivity::class.java)
                                        intent.putExtras(bundle)
                                        activity.startActivityForResult(intent,1)
                                    }
                                }

                                override fun onLongClicked(adapter: ListAdapter, index: Int) {
                                    var album = adapter.listItems[index] as Album
                                    if (!adapter.isSelection) {
                                        adapter.isSelection = true
                                        adapter.notifyDataSetChanged()
                                    }
                                    adapter.isSelection = true
                                    album.isSelected = true
                                    adapter.notifyItemChanged(index)
                                    (activity as HomeActivity).makeShareDeleteToolbar( adapter.listItems as List<Album>)
                                }

                            })

                        return hol
                    }
                    else -> {

                        val hol = AlbumsDirectoryViewHolder(view)
                        hol.setActivity(
                            activity,
                            adapter = adapter,
                            clickable = object : BaseViewHolder.Clickable {

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
        progressCircularAccent.visibility = View.GONE

        val glm = GridLayoutManager(activity, 3)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (adapter.getItemViewType(position)) {
                    R.layout.item_header_photos -> return 3
                    R.layout.item_header_album_directory ->return 3
                    else -> return 1
                }
            }
        }
        activity.recyclerPhotos.layoutManager = glm
        return adapter
    }

}
