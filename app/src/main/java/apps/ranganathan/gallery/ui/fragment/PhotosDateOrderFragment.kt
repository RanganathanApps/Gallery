package apps.ranganathan.gallery.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.model.ParentModel
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.viewmodel.PhotosViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class PhotosDateOrderFragment : Fragment() {


    private lateinit var data: ArrayList<Album>
    private lateinit var viewModel: PhotosViewModel
    private var mPhotosList: List<Album>? = null

    private var photosComparator: Comparator<Album>? = null

    private var recyclerView: RecyclerView? = null
    private lateinit var progressCircularAccent: View

    internal lateinit var adapter: ListAdapter

    public var mediaMounted: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.photos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<View>(R.id.recyclerPhotos) as RecyclerView
        progressCircularAccent = view.findViewById<View>(R.id.progressCircularAccent)
        viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)

        recyclerView!!.layoutManager = LinearLayoutManager(context)

        /*gridDividerDecoration = GridDividerDecoration(context)
        recyclerView!!.addItemDecoration(gridDividerDecoration!!)*/

        if (!::adapter.isInitialized) {
            loadFiles()
        } else {
            bindAdapter()
        }

        if (mediaMounted){
            mediaMounted = !mediaMounted
            loadFiles()
        }
    }

    public fun loadFiles() {
        progressCircularAccent.visibility = View.VISIBLE
        doAsync {
            mPhotosList = viewModel.getAllImages(activity!!.applicationContext)
            splitData()
            uiThread {
                bindAdapter()
            }
        }
    }


    private fun bindAdapter() {
        adapter = viewModel.setDataToAdapter(
            activity as BaseActivity,
            progressCircularAccent,
            data, "date"
        )
        recyclerView!!.adapter = adapter
        val glm = GridLayoutManager(activity, 3)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (adapter.getItemViewType(position)) {
                    R.layout.item_header_photos -> return 3
                    R.layout.item_header_album_directory -> return 3
                    else -> return 1
                }
            }
        }
        recyclerView!!.layoutManager = glm
    }

    private fun splitData() {
        this.photosComparator = kotlin.Comparator { o1, o2 -> o2.date.compareTo(o1.date) }

        Collections.sort(mPhotosList, photosComparator)


        data = arrayListOf()

        var dataModels = arrayListOf<ParentModel>()

        var tempModels = arrayListOf<Album>()

        var iterate = mPhotosList!!.iterator()
        var lastDate = ""
        lastDate = mPhotosList!![0].dateString
        var parentModel: ParentModel
        parentModel = ParentModel()
        parentModel.albums = arrayListOf()
        parentModel.isHeader = true
        dataModels.add(parentModel)
        tempModels.add(mPhotosList!![0])

        val albumDuplicateInial = Album()
        albumDuplicateInial.name = mPhotosList!![0].name
        albumDuplicateInial.dateString = mPhotosList!![0].dateString
        albumDuplicateInial.date = mPhotosList!![0].date
        albumDuplicateInial.albumUri = mPhotosList!![0].albumUri
        albumDuplicateInial.bucket = mPhotosList!![0].dateString
        albumDuplicateInial.isSectionHeader = true

        data.add(albumDuplicateInial)


        var albumDuplicate = Album()

        var isDateChanged: Boolean

        while (iterate.hasNext()) {

            val album = iterate.next()

            isDateChanged = album.dateString != lastDate

            if (!isDateChanged) {
                parentModel.albums.add(album)

            } else {
                parentModel = ParentModel()
                parentModel.albums = arrayListOf()
                album.isSectionHeader = false

                albumDuplicate = Album()
                albumDuplicate.name = album.name
                albumDuplicate.dateString = album.dateString
                albumDuplicate.date = album.date
                albumDuplicate.albumUri = album.albumUri
                // albumDuplicate.bucket = album.bucket
                albumDuplicate.isSectionHeader = true
                data.add(albumDuplicate)

                parentModel.albums.add(album)
                dataModels.add(parentModel)

                tempModels.add(albumDuplicate)
            }
            data.add(album)
            parentModel.header = lastDate
            lastDate = album.dateString

        }


        /*viewModel.setDataToAdapter(
            activity as BaseActivity,
            LinearLayoutManager(activity) as RecyclerView.LayoutManager,
            dataModels as ArrayList<Any>
        )*/

    }

    fun getAdapter(): ListAdapter {
        return adapter!!
    }

    private fun setAdapterByGenre() {

        this.photosComparator = kotlin.Comparator { o1, o2 -> o2.date.compareTo(o1.date) }

        Collections.sort(data, photosComparator)

    }




    fun deleteFile(context:Context): ArrayList<Album> {
        var list = arrayListOf<Album>()
        for (i in 0 until adapter.listItems.size) {
            if ((adapter.listItems[i] as Album).isSelected) {
                list.add((adapter.listItems[i] as Album))
                viewModel.delete(context,(adapter.listItems[i] as Album).file)
            }
        }
        return  list
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.clear()

        inflater!!.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.menu_sort_date).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        return true
    }
}
