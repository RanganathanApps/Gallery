package apps.ranganathan.gallery.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.progress_circle.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class PhotosFragment : Fragment() {


    companion object {

        fun newInstance() = PhotosFragment()

    }

    private lateinit var result: ArrayList<Album>
    internal lateinit var adapter: ListAdapter
    private var contentView: View? = null
    private lateinit var viewModel: PhotosViewModel

    public var mediaMounted: Boolean = false

    fun getAdapter():ListAdapter{
        return adapter
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
        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        }
        if (!::adapter.isInitialized) {
            loadPhotos()
        }else{
            initPhotos(result)
        }
        if (mediaMounted){
            mediaMounted = !mediaMounted
            loadPhotos()
        }
        //viewModel.loadAndBind(activity as Context,progressCircularAccent,recyclerPhotos)
    }

    internal fun loadPhotos() {
        contentView!!.findViewById<View>(R.id.progressCircularAccent).visibility = View.VISIBLE
        doAsync {
            result =  viewModel.getAllImages(activity!!.applicationContext)
            uiThread {
                initPhotos(result)
            }
        }
    }



    private fun initPhotos(files: ArrayList<Album>) {
        if (activity!=null) {
            adapter = viewModel.setDataToAdapter(activity as BaseActivity,progressCircularAccent,files,"photos")
            //viewModel.loadAndBind(activity as Context,progressCircularAccent,recyclerPhotos)
            adapter.setItems(files)
            recyclerPhotos.layoutManager = GridLayoutManager(activity, 3)
            recyclerPhotos.hasFixedSize()
            recyclerPhotos.adapter = adapter
        }
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
