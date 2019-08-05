package apps.ranganathan.gallery.viewmodel

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.os.Environment.getExternalStorageDirectory
import android.content.Intent
import android.os.Bundle
import apps.ranganathan.gallery.viewholders.*
import java.io.Serializable


class PhotosViewModel : HomeViewModel(), PictureViewActivity.DeleteListener<Any> {

    private lateinit var bundle: Bundle
    private lateinit var map: Map<String, Any>
    private lateinit var intent: Intent
    private lateinit var album: Album

    override fun onDeleted(index: Int, data: Any) {

       /* data as MutableList<Album>
        if (data.isNotEmpty()) {
            data.forEachIndexed { index, album ->
                val item = adapter.listItems.find { (it as Album).albumUri.equals(album.albumUri) }
                if (item != null)
                    (item as Album).isSelected = true
            }
            adapter.deleteItems()
            *//*for (i in 0 until adapter.listItems.size) {
                   if ((adapter.listItems[i] as Album).albumUri.equals(album.albumUri)) {
                       (adapter.listItems[i]  as Album).isSelected = true
                       break
                   }

               }*//*

        }*/
    }

    private lateinit var resultPhotos: ArrayList<Album>
    private lateinit var adapter: ListAdapter

    fun loadAndBind(
        context: Context,
        progressCircularAccent: ProgressBar,
        recyclerView: RecyclerView
    ) {
        if (!::resultPhotos.isInitialized) {
            doAsync {
                resultPhotos = getAllImages(context.applicationContext)
                uiThread {
                   // setPhotosToAdapter(context, resultPhotos)
                    adapter.setItems(resultPhotos)
                    bindPhotos(context, resultPhotos, progressCircularAccent, recyclerView)
                }
            }
        } else {
            bindPhotos(context, resultPhotos, progressCircularAccent, recyclerView)
        }
    }

    private fun bindPhotos(
        context: Context, results: ArrayList<Album>, progressCircularAccent: View,
        recyclerView: RecyclerView
    ) {

        recyclerView.layoutManager = GridLayoutManager(context, 3) as RecyclerView.LayoutManager
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter
        progressCircularAccent.visibility = GONE
    }




    internal fun setDataToAdapter(
        activity: BaseActivity,
        tag: String
    ): ListAdapter {

        adapter = object : ListAdapter() {

            override fun getLayoutId(position: Int, obj: Any): Int {
                val album = obj as Album
                return when (album.isSectionHeader) {
                    true -> {
                        try {
                            if (album.bucket.isNotEmpty()) {
                                if ((adapter.listItems[position+1] as Album).dateString == album.dateString) {
                                    R.layout.item_header_photos
                                }else{
                                    (adapter.listItems as MutableList<Any>).remove(album)
                                    R.layout.item_header_empty
                                }
                            } else
                                R.layout.item_header_photos
                        } catch (e: UninitializedPropertyAccessException) {
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
                            activity as BaseActivity,
                            adapter = adapter,
                            clickable = object : BaseViewHolder.Clickable {

                                override fun clicked(adapter: ListAdapter, index: Int) {
                                    album = adapter.listItems[index] as Album
                                    if (adapter.isSelection) {
                                        album.isSelected = !album.isSelected
                                        adapter.notifyItemChanged(index)
                                        (activity as HomeActivity).makeShareDeleteToolbar(adapter.listItems)
                                    } else {
                                        map = mapOf("tag" to tag,"position" to index,"album" to album)
                                        bundle = Bundle()
                                        for (pair in map) {
                                            bundle.putSerializable(pair.key, pair.value as Serializable)
                                        }
                                        intent = Intent(activity,PictureViewActivity::class.java)
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
                                    (activity as HomeActivity).makeShareDeleteToolbar(adapter.listItems as List<Album>)
                                }

                            })

                        return hol
                    }
                    R.layout.item_header_photos -> {

                        val hol = HeaderViewHolder(view)
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
                    R.layout.item_header_album_directory  -> {

                        val hol = AlbumsDirectoryViewHolder(view)
                        hol.setActivity(
                            activity as BaseActivity,
                            adapter = adapter,
                            clickable = object : BaseViewHolder.Clickable {

                                override fun clicked(adapter: ListAdapter, index: Int) {

                                }

                                override fun onLongClicked(adapter: ListAdapter, index: Int) {

                                }

                            })

                        return hol
                    }else -> {
                    val hol = EmptyViewHolder(view)
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
        return adapter
    }

}
