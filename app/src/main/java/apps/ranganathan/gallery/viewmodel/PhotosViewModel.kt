package apps.ranganathan.gallery.viewmodel

import android.content.Context
import android.view.View
import android.widget.ProgressBar
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
import apps.ranganathan.gallery.viewholders.HeaderViewHolder
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.progress_circle.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList


class PhotosViewModel : HomeViewModel() {

    private lateinit var resultPhotos: ArrayList<Album>
    private lateinit var adapter: ListAdapter

    fun loadAndBind(context: Context,
                    progressCircularAccent: ProgressBar,
                    recyclerView: RecyclerView){
        if (!::resultPhotos.isInitialized) {
            doAsync {
                resultPhotos = getAllImages(context!!.applicationContext)
                uiThread {
                    setPhotosToAdapter(context,resultPhotos)
                    adapter.setItems(resultPhotos)
                    bindPhotos(context, resultPhotos, progressCircularAccent, recyclerView)
                }
            }
        }else{
            bindPhotos(context, resultPhotos, progressCircularAccent, recyclerView)
        }
    }

    private fun bindPhotos(context: Context,results: ArrayList<Album>,progressCircularAccent: View,
                           recyclerView: RecyclerView) {

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter
        progressCircularAccent.visibility = View.GONE
    }

    private fun setPhotosToAdapter(activity:Context,files: ArrayList<Album>) :ListAdapter {

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
                        return HeaderViewHolder(view)
                    }
                }
            }
        }
        return adapter


    }



    internal fun setDataToAdapter(
        activity: BaseActivity,
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
                        val hol = AlbumViewHolder(view)
                        hol.setActivity(
                            activity as BaseActivity,
                            adapter = adapter,
                            clickable = object : BaseViewHolder.Clickable {

                                override fun clicked(adapter: ListAdapter, index: Int) {
                                    var album = adapter.listItems[index] as Album
                                    if (adapter.isSelection) {
                                        album.isSelected = !album.isSelected
                                        adapter.notifyItemChanged(index)
                                        (activity as HomeActivity).makeShareaDeleteToolbar(
                                            adapter,
                                            null,
                                            adapter.listItems as List<Album>
                                        )
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
                                    var album = adapter.listItems[index] as Album
                                    if (!adapter.isSelection) {
                                        adapter.isSelection = true
                                        adapter.notifyDataSetChanged()
                                    }
                                    adapter.isSelection = true
                                    album.isSelected = true
                                    adapter.notifyItemChanged(index)
                                    (activity as HomeActivity).makeShareaDeleteToolbar(
                                        adapter,
                                        null,
                                        adapter.listItems as List<Album>
                                    )
                                }

                            })

                        return hol
                    }
                    R.layout.item_header_photos-> {

                        val hol = HeaderViewHolder(view)
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
                    }
                    else -> {

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
                    }
                }
            }
        }


        adapter.setItems(files)
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
