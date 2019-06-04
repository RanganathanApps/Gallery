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
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.photos_fragment.*

class PhotosFragment : Fragment() {

    companion object {
        fun newInstance() = PhotosFragment()
    }

    private lateinit var viewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        if (recyclerPhotos.adapter == null)
            initPhotos(viewModel.getAllImages(activity!!.applicationContext))
    }

    private fun initPhotos(files: List<Album>) {

        val adapter = PhotosAdapter(activity!! as BaseActivity, files)
        recyclerPhotos.layoutManager = GridLayoutManager(activity!! as BaseActivity, 4) as RecyclerView.LayoutManager?
        recyclerPhotos.setHasFixedSize(true)
        recyclerPhotos.adapter = adapter


    }

}
