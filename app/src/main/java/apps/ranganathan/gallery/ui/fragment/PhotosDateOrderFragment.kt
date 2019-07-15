package apps.ranganathan.gallery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import java.util.Collections
import java.util.Comparator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.BaseSectionAdapter
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.adapter.PhotosAdapterByDate
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.utils.GridDividerDecoration

class PhotosDateOrderFragment : Fragment(), BaseSectionAdapter.OnItemClickListener {

    override fun onItemSelected(albums: List<Album>, position: Int) {
        (activity as HomeActivity).makeShareaDeleteToolbar(null,mSectionedRecyclerAdapter,mPhotosList!!)
    }


    private lateinit var viewModel: PhotosViewModel
    private var mPhotosList: List<Album>? = null

    private var photosComparator: Comparator<Album>? = null

    private var recyclerView: RecyclerView? = null

    internal var mSectionedRecyclerAdapter: BaseSectionAdapter? = null

    private var gridDividerDecoration: GridDividerDecoration? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.photos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<View>(R.id.recyclerPhotos) as RecyclerView
        viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)

        recyclerView!!.layoutManager = LinearLayoutManager(context)

        gridDividerDecoration = GridDividerDecoration(context)
        recyclerView!!.addItemDecoration(gridDividerDecoration!!)


        mPhotosList=  viewModel.getAllImages(activity!!.applicationContext)


        setAdapterWithGridLayout()

        mSectionedRecyclerAdapter!!.setOnItemClickListener(this)

        recyclerView!!.adapter = mSectionedRecyclerAdapter
    }

    fun getAdapter(): BaseSectionAdapter {
        return mSectionedRecyclerAdapter!!
    }

    private fun setAdapterByGenre() {

        this.photosComparator = kotlin.Comparator { o1, o2 ->  o2.date!!.compareTo(o1.date!!)}

        Collections.sort(mPhotosList, photosComparator)
        mSectionedRecyclerAdapter = PhotosAdapterByDate(activity  as BaseActivity,mPhotosList!!)
    }


    private fun setAdapterWithGridLayout() {
        setAdapterByGenre()
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView!!.layoutManager = gridLayoutManager
        mSectionedRecyclerAdapter!!.setGridLayoutManager(gridLayoutManager)
    }

    override fun onItemClicked(album: Album, position: Int) {

        val anotherMap = mapOf("tag" to "date",
            "date" to album.dateString,"position" to position,"count" to album.count,
            "album" to album)
        (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java,anotherMap)
        /*val index = mPhotosList!!.indexOf(album)
        mPhotosList!!.remove(movie)
        mSectionedRecyclerAdapter!!.notifyItemRemovedAtPosition(index)*/
    }

    override fun onSubheaderClicked(position: Int) {
        if (mSectionedRecyclerAdapter!!.isSectionExpanded(mSectionedRecyclerAdapter!!.getSectionIndex(position))) {
            mSectionedRecyclerAdapter!!.collapseSection(mSectionedRecyclerAdapter!!.getSectionIndex(position))
        } else {
            mSectionedRecyclerAdapter!!.expandSection(mSectionedRecyclerAdapter!!.getSectionIndex(position))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        return true
    }
}
