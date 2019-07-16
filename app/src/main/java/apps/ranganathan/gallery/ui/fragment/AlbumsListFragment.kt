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
import apps.ranganathan.gallery.adapter.AlbumsAdapterByList
import apps.ranganathan.gallery.adapter.BaseSectionAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.utils.GridDividerDecoration

class AlbumsListFragment : Fragment(), BaseSectionAdapter.OnItemClickListener {

    override fun onItemSelected(position: Int) {
        (activity as HomeActivity).makeShareaDeleteToolbar(null,mSectionedRecyclerAdapter,mAlbumsList!!)
    }



    private lateinit var viewModel: PhotosViewModel
    private var mAlbumsList: List<Album>? = null

    private var photosComparator: Comparator<Album>? = null

    private var recyclerView: RecyclerView? = null

    internal var mSectionedRecyclerAdapter: BaseSectionAdapter? = null

    private var gridDividerDecoration: GridDividerDecoration? = null


    fun getAdapter(): BaseSectionAdapter {
        return mSectionedRecyclerAdapter!!
    }


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


        mAlbumsList=  viewModel.getAllImages(activity!!.applicationContext)


        setAdapterWithGridLayout()

        mSectionedRecyclerAdapter!!.setOnItemClickListener(this)

        recyclerView!!.adapter = mSectionedRecyclerAdapter
    }


    private fun setAdapterByGenre() {

        this.photosComparator = kotlin.Comparator { o1, o2 ->  o1.bucket.capitalize()!!.compareTo(o2.bucket.capitalize()!!)}

        Collections.sort(mAlbumsList, photosComparator)
        mSectionedRecyclerAdapter = AlbumsAdapterByList(activity  as BaseActivity,mAlbumsList!!)
    }


    private fun setAdapterWithGridLayout() {
        setAdapterByGenre()
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView!!.layoutManager = gridLayoutManager
        mSectionedRecyclerAdapter!!.setGridLayoutManager(gridLayoutManager)
    }

    override fun onItemClicked(album: Album, position: Int) {

        val anotherMap = mapOf("tag" to "albums_list",
            "bucket" to album.bucket,"position" to position,"count" to album.count,
            "album" to album)
        (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java,anotherMap)
        /*val anotherMap = mapOf("position" to position, "tag" to "camera","directory" to "/${album.bucket}","directory_ui" to album.bucket)
        (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java,anotherMap)*/
    }

    override fun onSubheaderClicked(position: Int) {
        if (mSectionedRecyclerAdapter!!.isSectionExpanded(mSectionedRecyclerAdapter!!.getSectionIndex(position))) {
            mSectionedRecyclerAdapter!!.collapseSection(mSectionedRecyclerAdapter!!.getSectionIndex(position))
        } else {
            mSectionedRecyclerAdapter!!.expandSection(mSectionedRecyclerAdapter!!.getSectionIndex(position))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //Empty the old menu
        menu!!.clear()

        inflater!!.inflate(R.menu.menu_album_list, menu)
        menu!!.findItem(R.id.menu_albums_list).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        return true
    }
}
