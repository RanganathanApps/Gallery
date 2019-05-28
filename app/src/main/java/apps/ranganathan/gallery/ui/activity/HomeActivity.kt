package apps.ranganathan.gallery.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.AlbumsAdapter
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.HomeViewModel

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.toolbar_home.*


class HomeActivity : BaseActivity() {

    private lateinit var homeVieModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbarHome)
        setConnectivityChange()


        homeVieModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


        action_bar_spinner.adapter = homeVieModel.setTypes(this)
        fabCamera.setOnClickListener { view ->
           takePhoto(object : ImagePickerListener{
               override fun onPicked(bitmap: Bitmap) {
                   showToast(""+bitmap.height.toString())

               }
           })
        }


        action_bar_spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(action_bar_spinner.selectedItem.toString()){
                    "Albums"->{
                        initAlbum(homeVieModel.getAlbums(this@HomeActivity))
                    }
                    "Photos" ->{
                        initPhotos(homeVieModel.getAllImages(this@HomeActivity))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                initPhotos(homeVieModel.getAllImages(this@HomeActivity))

            }

        })



    }

    private lateinit var albumm: Album

    private fun initPhotos(files: List<Album>) {

        //recyclerAlbums.layoutManager = GridLayoutManager(this,  2) as RecyclerView.LayoutManager?
       //recyclerAlbums.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

       /* val glm = GridLayoutManager(this, 3)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position % 3 == 2) {
                    return 3
                }
                when (position % 4) {
                    1, 3 -> return 1
                    0, 2 -> return 2
                    else ->
                        //never gonna happen
                        return -1
                }
            }
        }
        recyclerAlbums.setLayoutManager(glm)*/



        val adapter = PhotosAdapter(this,files)

        //now adding the adapter to recyclerview
        recyclerAlbums.adapter = adapter
        //recyclerAlbums.setHasFixedSize(true)
        //recyclerAlbums.setItemViewCacheSize(20)
        //recyclerAlbums.setDrawingCacheEnabled(true)
    }


    private fun initAlbum(files: List<Album>) {

        recyclerAlbums.layoutManager = GridLayoutManager(this,  2) as RecyclerView.LayoutManager?
        recyclerAlbums.layoutManager = StaggeredGridLayoutManager(2,VERTICAL)

        val adapter = AlbumsAdapter(this,files)

        //now adding the adapter to recyclerview
        recyclerAlbums.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            else -> super.onOptionsItemSelected(item)

        }
    }
}
