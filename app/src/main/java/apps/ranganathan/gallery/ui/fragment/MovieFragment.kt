package apps.ranganathan.gallery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.BaseMovieAdapter
import apps.ranganathan.gallery.adapter.MovieAdapterByGenre
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.utils.GridDividerDecoration

class MovieFragment : Fragment(), BaseMovieAdapter.OnItemClickListener {


    private lateinit var viewModel: PhotosViewModel
    private var mMovieList: List<Album>? = null

    private var movieComparator: Comparator<Album>? = null

    private var recyclerView: RecyclerView? = null

    private var mSectionedRecyclerAdapter: BaseMovieAdapter? = null

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

        val resources = resources
        val names = resources.getStringArray(R.array.names)
        val genres = resources.getStringArray(R.array.genres)
        val years = resources.getIntArray(R.array.years)

        mMovieList = ArrayList(20)

       /* for (i in 0..19) {
            val movie = Album(names[i], years[i], genres[i])
            mMovieList!!.add(movie)
        }*/
        mMovieList=  viewModel.getAllImages(activity!!.applicationContext)


        setAdapterWithGridLayout()

        mSectionedRecyclerAdapter!!.setOnItemClickListener(this)

        recyclerView!!.adapter = mSectionedRecyclerAdapter
    }


    private fun setAdapterByGenre() {

        this.movieComparator = kotlin.Comparator { o1, o2 ->  o1.name!!.compareTo(o2.name!!)}

        Collections.sort(mMovieList, movieComparator)
        mSectionedRecyclerAdapter = MovieAdapterByGenre(activity  as BaseActivity,mMovieList!!)
    }


    private fun setAdapterWithGridLayout() {
        setAdapterByGenre()
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView!!.layoutManager = gridLayoutManager
        mSectionedRecyclerAdapter!!.setGridLayoutManager(gridLayoutManager)
    }

    override fun onItemClicked(album: Album, position: Int) {

        val anotherMap = mapOf("tag" to "date","directory" to album.dateString,"position" to position,"count" to album.count,"album" to album)
        (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java,anotherMap)
        /*val index = mMovieList!!.indexOf(album)
        mMovieList!!.remove(movie)
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
