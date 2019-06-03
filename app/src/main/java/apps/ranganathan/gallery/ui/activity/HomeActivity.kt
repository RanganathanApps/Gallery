package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.AlbumsAdapter
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_drawer.*

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView




class HomeActivity : BaseActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_photos ->{
                initPhotos(homeVieModel.getAllImages(this@HomeActivity))
            }
            R.id.action_albums ->{
                initAlbum(homeVieModel.getAllImages(this@HomeActivity))
            }
            R.id.action_settings ->{
                initAlbum(homeVieModel.getAllImages(this@HomeActivity))
            }
            R.id.action_camera ->{
                takePhoto(object : ImagePickerListener{
                    override fun onPicked(bitmap: Bitmap) {
                        showToast(""+bitmap.height.toString())

                    }
                })
            }
        }
        return true
    }

    private lateinit var homeVieModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbarHome)
        setConnectivityChange()

        homeVieModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        getPermission(this,object :PermissionListener{
            override fun onDenied(deniedPermissions: List<String>) {

            }

            override fun onDeniedForeEver(deniedPermissions: List<String>) {
            }

            override fun onGranted() {

                val view = navigation.findViewById(R.id.action_photos) as View
                view.performClick()
            }
        },Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)

        setNavDrawer()

        fabAnimation(recyclerAlbums)

        setNavigation()
    }

    private fun setNavigation() {
        navigation.setOnNavigationItemSelectedListener(this)
    }

    /*navigation bar Icons and drawer toogle*/
    private fun setNavDrawer() {

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbarHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.drawerArrowDrawable.color = ContextCompat.getColor(context, R.color.colorWhite)
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

        recyclerAlbums.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //now adding the adapter to recyclerview
        recyclerAlbums.setHasFixedSize(true)
        recyclerAlbums.adapter = adapter
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


    private fun fabAnimation(recyclerView: RecyclerView){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fabCamera.isShown) {
                    fabCamera.hide()
                } else if (dy < 0 && fabCamera.visibility !== View.VISIBLE) {
                    fabCamera.show()
                }
            }
        })

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
