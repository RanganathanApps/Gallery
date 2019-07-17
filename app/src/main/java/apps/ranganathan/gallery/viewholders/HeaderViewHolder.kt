package apps.ranganathan.gallery.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.model.ParentModel
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import java.util.*

class HeaderViewHolder : BaseViewHolder {

    private lateinit var parentModel: ParentModel
    var txtAlbum: AppCompatTextView
    var childRecyclerView: RecyclerView
    lateinit var adapter1 : ListAdapter
    lateinit var hVHolder : HeaderViewHolder

    constructor(itemView: View) : super(itemView) {
        txtAlbum = itemView.findViewById(R.id.txtAlbum) as AppCompatTextView
        childRecyclerView = itemView.findViewById(R.id.recyclerPhotos) as RecyclerView

    }

    override fun bind(holder: RecyclerView.ViewHolder,data: Any) {

        hVHolder = (holder as HeaderViewHolder)
        parentModel = data as ParentModel
        txtAlbum.text = parentModel.header

        if (!::adapter1.isInitialized)
            hVHolder.adapter1 = setDataToAdapter(holder,
            childRecyclerView,
            GridLayoutManager(activity, 3) as RecyclerView.LayoutManager,
            parentModel.albums as ArrayList<Any>
        )



    }

    internal fun setDataToAdapter(
        holder: RecyclerView.ViewHolder,childRecyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager,files: ArrayList<Any> ): ListAdapter {

            (holder as HeaderViewHolder).adapter1  = object : ListAdapter() {

            override fun getLayoutId(position: Int, obj: Any): Int {
                return when (obj) {
                    is Album -> {
                        R.layout.item_photos
                    }
                    else -> {
                        R.layout.item_photos
                    }
                }
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                when (viewType) {
                    R.layout.item_photos -> {
                        val hol = AlbumViewHolder(view)
                        (holder as HeaderViewHolder).adapter1 ?.let {
                            hol.setActivity(
                                activity as BaseActivity,
                                adapter = it,
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
                                            val anotherMap = mapOf("tag" to "date",
                                                "date" to album.dateString,"position" to index,"count" to listItems.size,
                                                "album" to album)
                                            (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java,anotherMap)
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
                        }

                        return hol
                    }

                }
                return null!!
            }
        }


        (holder as HeaderViewHolder).adapter1 .setItems(files)
        childRecyclerView.layoutManager = layoutManager
        childRecyclerView.hasFixedSize()
        childRecyclerView.adapter = (holder as HeaderViewHolder).adapter1
        return (holder as HeaderViewHolder).adapter1 !!

    }

}