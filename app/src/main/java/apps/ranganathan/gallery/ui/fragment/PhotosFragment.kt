package apps.ranganathan.gallery.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.viewholders.AlbumViewHolder
import apps.ranganathan.gallery.viewholders.BaseViewHolder
import apps.ranganathan.gallery.viewholders.HeaderViewHolder
import apps.ranganathan.gallery.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.progress_circle.*


class PhotosFragment : Fragment() {


    companion object {

        fun newInstance() = PhotosFragment()

    }

    internal lateinit var adapter: ListAdapter
    private var contentView: View? = null
    private lateinit var viewModel: PhotosViewModel

    fun getAdapter():ListAdapter{
        return adapter
    }
    fun deleteFile(context:Context){
        for (i in 0 until adapter.listItems.size) {
            if ((adapter.listItems[i] as Album).isSelected) {
                viewModel.delete(context,(adapter.listItems[i] as Album).file)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.photos_fragment, container, false)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        if (recyclerPhotos.adapter == null) {
            loadPhotos()
        }
    }

    internal fun loadPhotos() {
        initPhotos(viewModel.getAllImages(activity!!.applicationContext))
    }

    internal fun updatePhotos() {
        initPhotos(viewModel.getAllImages(activity!!.applicationContext))
    }

    private fun initPhotos(files: ArrayList<Album>) {
        setDataToAdapter(files)
    }

    private fun setDataToAdapter(files: ArrayList<Album>) {

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

        adapter.setItems(files)
        recyclerPhotos.layoutManager = GridLayoutManager(activity, 3)
        recyclerPhotos.hasFixedSize()
        recyclerPhotos.adapter = adapter
        progressCircular.visibility = GONE
    }



    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //Empty the old menu
        menu!!.clear()

        inflater!!.inflate(R.menu.menu_main, menu)
        menu!!.findItem(R.id.menu_all_photos).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return true
    }
}
