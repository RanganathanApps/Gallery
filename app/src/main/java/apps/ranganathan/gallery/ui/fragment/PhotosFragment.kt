package apps.ranganathan.gallery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.utils.PhotoSelectedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.photos_fragment.*

class PhotosFragment : Fragment() {


    companion object {

        fun newInstance() = PhotosFragment()

    }

    private var contentView: View? = null
    private lateinit var viewModel: PhotosViewModel



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
        if (recyclerPhotos.adapter == null)
            initPhotos(viewModel.getAllImages(activity!!.applicationContext))
    }

    private fun initPhotos(files: List<Album>) {

       //val k =  files.sortedWith(compareBy<Album> { it.file.lastModified() }.thenBy { it.file.lastModified() })

        val adapter = PhotosAdapter(activity!! as BaseActivity, files,photoSelctedListener = object :
            PhotoSelectedListener {
            override fun onItemSelected(position: Int, list: List<Album>) {
                
            }

            override fun onPhotoSelected(position: Int, list: List<Album>) {
                val anotherMap = mapOf("position" to position, "tag" to "photos")
                (activity as BaseActivity).startActivityputExtra(activity as BaseActivity, PictureViewActivity::class.java, anotherMap)
            }
        })
        recyclerPhotos.layoutManager = GridLayoutManager(activity!! as BaseActivity, 3) as RecyclerView.LayoutManager?
        recyclerPhotos.setHasFixedSize(true)
        recyclerPhotos.adapter = adapter
        //viewModel.makeHideShow(recyclerPhotos,navigation = (activity as HomeActivity).getBottomNavigationView())


    }

}
