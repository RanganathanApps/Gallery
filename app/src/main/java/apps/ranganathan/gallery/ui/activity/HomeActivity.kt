package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_drawer.*

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import android.view.animation.TranslateAnimation




class HomeActivity : BaseActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_photos ->{
                initPhotos(homeVieModel.getAllImages(this@HomeActivity))
            }
            R.id.action_albums ->{
                initAlbum(homeVieModel.getAlbums(this@HomeActivity))
            }
            R.id.action_favourites ->{

            }
            R.id.action_camera ->{
                takePhoto(object : ImagePickerListener{
                    override fun onCancelled() {

                    }

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
        setSupportActionBar(toolbar)
        setConnectivityChange()

        homeVieModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        getPermission(this,object :PermissionListener{
            override fun onDenied(deniedPermissions: List<String>) {

            }

            override fun onDeniedForeEver(deniedPermissions: List<String>) {
            }

            override fun onGranted() {
                initPhotos(homeVieModel.getAllImages(this@HomeActivity))
            }
        },Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)

        setNavDrawer()

        setNavigation()
    }

    private fun setNavigation() {
        navigation.setOnNavigationItemSelectedListener(this)
    }

    /*navigation bar Icons and drawer toogle*/
    private fun setNavDrawer() {

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
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

    protected fun makeHideShow(recyclerView : RecyclerView){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && navigation.isShown) {
                    //navigation.visibility = View.GONE
                    slideDown(navigation)
                    //hideBottomNavigationView(navigation)
                } else if (dy < 0) {
                     //navigation.visibility = View.VISIBLE
                    slideUp(navigation)
                    //showBottomNavigationView(navigation)

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f, // fromXDelta
            0f, // toXDelta
            view.height.toFloat(), // fromYDelta
            0f
        )                // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        view.visibility = View.GONE
        val animate = TranslateAnimation(
            0f, // fromXDelta
            0f, // toXDelta
            0f, // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0f)
    }
    private fun initAlbum(files: List<Album>) {

        recyclerAlbums.layoutManager = GridLayoutManager(this,  2) as RecyclerView.LayoutManager?
        recyclerAlbums.layoutManager = StaggeredGridLayoutManager(2,VERTICAL)

        val adapter = AlbumsAdapter(this,files)

        //now adding the adapter to recyclerview
        recyclerAlbums.adapter = adapter

        makeHideShow(recyclerAlbums)


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
