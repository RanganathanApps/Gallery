package apps.ranganathan.gallery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import apps.ranganathan.gallery.utils.PhotoSelectedListener
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.toolbar_home.*


class PhotosFragment : Fragment() {


    companion object {

        fun newInstance() = PhotosFragment()

    }

    private lateinit var adapter: PhotosAdapter
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

        adapter = PhotosAdapter(activity!! as BaseActivity, files, photoSelctedListener = object :
            PhotoSelectedListener {
            override fun onItemSelected(position: Int, list: List<Album>) {
                var count = 0
                for (item in list) {
                    if (item.isSelected) {
                        count += 1
                    }
                }
                (activity as BaseActivity).setToolBarTitle("" + count)
                (activity as BaseActivity).toolbar.navigationIcon =
                    ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.ic_clear_white_24dp)
                val params = (activity as BaseActivity).toolbar.getLayoutParams() as AppBarLayout.LayoutParams
                params.scrollFlags = 0
                (activity as BaseActivity).toolbar.setNavigationOnClickListener {
                    adapter.isSelection = false
                    adapter.notifyDataSetChanged()
                    (activity as BaseActivity).setAppBar("Photos")
                }
               // params.scrollFlags =  AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }

            override fun onPhotoSelected(position: Int, list: List<Album>) {
                val anotherMap = mapOf("position" to position, "tag" to "photos")
                (activity as BaseActivity).startActivityputExtra(
                    activity as BaseActivity,
                    PictureViewActivity::class.java,
                    anotherMap
                )
            }
        })
        recyclerPhotos.layoutManager = GridLayoutManager(activity!! as BaseActivity, 3) as RecyclerView.LayoutManager?
        recyclerPhotos.setHasFixedSize(true)
        recyclerPhotos.adapter = adapter
        //viewModel.makeHideShow(recyclerPhotos,navigation = (activity as HomeActivity).getBottomNavigationView())


    }

}
