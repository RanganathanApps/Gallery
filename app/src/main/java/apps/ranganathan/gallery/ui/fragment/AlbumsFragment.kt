package apps.ranganathan.gallery.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.AlbumsAdapter
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.photos_fragment.*

class AlbumsFragment : Fragment() {

    companion object {
        fun newInstance() = AlbumsFragment()
    }

    private lateinit var viewModel: PhotosViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.albums_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        if (recyclerAlbums.adapter == null)
            initAlbums(viewModel.getAlbums(activity!!.applicationContext))
    }

    private fun initAlbums(files: List<Album>) {

        val adapter = AlbumsAdapter(activity!! as BaseActivity, files)
        recyclerAlbums.layoutManager = GridLayoutManager(activity!! as BaseActivity, 3) as RecyclerView.LayoutManager?
        recyclerAlbums.setHasFixedSize(true)
        recyclerAlbums.adapter = adapter
       // viewModel.makeHideShow(recyclerAlbums,navigation = navigation)

    }

}
