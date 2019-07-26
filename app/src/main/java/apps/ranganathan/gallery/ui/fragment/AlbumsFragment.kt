package apps.ranganathan.gallery.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.AlbumsAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.albums_fragment.*
import android.view.Menu
import android.view.MenuInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.viewmodel.PhotosViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class AlbumsFragment : Fragment() {

    companion object {
        fun newInstance() = AlbumsFragment()
    }

    private lateinit var result: List<Album>
    private var contentView: View? = null

    private lateinit var viewModel: PhotosViewModel
    private lateinit var adapter: AlbumsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        //Empty the old menu
        menu!!.clear()

        inflater!!.inflate(R.menu.menu_album_list, menu)
        menu!!.findItem(R.id.menu_grid).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.albums_fragment, container, false)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        if (!::adapter.isInitialized) {
            // Calling the AsyncTask
            //SimpleAsyncTask(viewModel,activity as BaseActivity,contentView).execute()
            view!!.findViewById<View>(R.id.progressCircularAccent).visibility = VISIBLE
            doAsync {
                result =  viewModel.getAlbums(activity!!.applicationContext)
                uiThread {
                   // toast(result)
                    initAlbums(result)
                }
            }
        }else{
            initAlbums(result)
        }
    }

    internal class SimpleAsyncTask(
        private val viewModel: PhotosViewModel,
        private val activity: BaseActivity,
        private val contentView: View?
    ) :
        AsyncTask<Void, Void, List<Album>>() {
        // do things with output
        override fun onPreExecute() {
            contentView!!.findViewById<View>(R.id.progressCircularAccent).visibility = VISIBLE
        }
        // Execute long task here
        override fun doInBackground(vararg voids: Void): List<Album> {
            return viewModel.getAlbums(activity.applicationContext)
        }
        // do things with output
        override fun onPostExecute(result: List<Album>) {
            initAlbums(result)
        }

        private fun initAlbums(files: List<Album>) {
            val adapter = AlbumsAdapter(activity,  files)
            activity.recyclerAlbums.layoutManager = GridLayoutManager(activity , 3)
            activity.recyclerAlbums.setHasFixedSize(true)
            activity.recyclerAlbums.adapter = adapter
            contentView!!.findViewById<View>(R.id.progressCircularAccent).visibility = GONE
        }
    }


    private fun initAlbums(files: List<Album>) {

        if (activity!=null) {
            adapter = AlbumsAdapter(activity!! as BaseActivity, files)
            recyclerAlbums.layoutManager =
                GridLayoutManager(activity!! as BaseActivity, 3) as RecyclerView.LayoutManager?
            recyclerAlbums.setHasFixedSize(true)
            recyclerAlbums.adapter = adapter
            contentView!!.findViewById<View>(R.id.progressCircularAccent).visibility = GONE
        }


        //viewModel.makeHideShow(recyclerAlbums, navigation = (activity as HomeActivity).getBottomNavigationView())

    }

}
