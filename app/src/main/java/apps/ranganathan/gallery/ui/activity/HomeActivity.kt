package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.configlibrary.utils.Utils
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.BaseMovieAdapter
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.adapter.PhotosAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.fragment.*
import apps.ranganathan.gallery.utils.BottomNavigationBehavior
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_picture_view.*
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.toolbar_home.*
import kotlinx.android.synthetic.main.toolbar_home_share_delete.*
import java.net.URI


class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    private var sortBy: Int = 0
    private lateinit var homeVieModel: HomeViewModel
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var photosFragment: PhotosFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var photosDateOrderFragment: PhotosDateOrderFragment
    private lateinit var albumsListFragment: AlbumsListFragment
    private lateinit var curentFragment: Fragment

    private var doubleBackToExitPressedOnce = false

    var  photosAdapter: ListAdapter? = null
    private var  baseAlbumsAdapter: BaseMovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        setConnectivityChange()
        initCode()
    }

    private fun initCode() {
        homeVieModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        setNavDrawer()
        setNavigation()

        getPermission(this, object : PermissionListener {
            override fun onDenied(deniedPermissions: List<String>) {
                showMsg(navigation, "Permissions are required to run this app")
            }

            override fun onDeniedForeEver(deniedPermissions: List<String>) {
                showMsg(navigation, "Enable Permissions on settings")
            }

            override fun onGranted() {
                sortBy = 0
                moveToAllPhotos()

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        imgShareToolbar.setOnClickListener {

            if (photosAdapter!=null) {
                shareMultileFiles(photosAdapter!!.listItems as List<Album>)
            }
            if (baseAlbumsAdapter!=null) {
                shareMultileFiles(baseAlbumsAdapter!!.movieList)
            }
        }
        imgDeleteToolbar.setOnClickListener {
            deleteMultipleFiles()

        }


    }

    private fun deleteMultipleFiles(){
        showConfirmationAlert(
            "Delete",
            "Image will be deleted permanently. do you want to continue?",
            object : Utils.OnClickListener {
                override fun onClick(v: View) {
                    photosFragment.adapter.deleteItems(6)
                }
            },
            object : Utils.OnClickListener {
                override fun onClick(v: View) {
                }
            })

    }


    private fun shareMultileFiles(list: List<Album>){
        var uris = ArrayList<Uri> ()

        for (item in list){
            if (item.isSelected) {
                val apkURI = FileProvider.getUriForFile(
                    context,
                    context.applicationContext
                        .packageName + ".provider", item.file
                )
                uris.add(apkURI)
            }
        }
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uris)
        startActivity(Intent.createChooser(shareIntent, "Share using"))
    }


    private fun moveToDateWise() {
        sortBy = 1

        if (!::photosDateOrderFragment.isInitialized) {
            photosDateOrderFragment = PhotosDateOrderFragment()

        }

        if (curentFragment == photosDateOrderFragment) {
            return
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, photosDateOrderFragment, "Photos")
            .commit()
        curentFragment = photosDateOrderFragment
    }

    private fun moveToAllPhotos() {

        if (!::photosFragment.isInitialized) {
            photosFragment = PhotosFragment.newInstance()
        }

        if (::curentFragment.isInitialized && curentFragment == photosFragment) {
            return
        }

        if (sortBy == 1) {
            moveToDateWise()
            return
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, photosFragment, "Photos").commit()
        curentFragment = photosFragment
    }

    private fun moveToListAlbums() {

        if (!::albumsListFragment.isInitialized) {
            albumsListFragment = AlbumsListFragment()
        }

        if (::curentFragment.isInitialized && curentFragment == albumsListFragment) {
            return
        }

        if (sortBy == 1) {
            moveToDateWise()
            return
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, albumsListFragment, "Photos")
            .commit()
        curentFragment = albumsListFragment
    }

    private fun moveToAlbums() {


        if (!::albumsFragment.isInitialized) {
            albumsFragment = AlbumsFragment.newInstance()
        }

        if (curentFragment == albumsFragment) {
            return
        }
        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, albumsFragment, "Albums").commit()
        curentFragment = albumsFragment
    }

    private fun moveToCamera() {


        if (!::cameraFragment.isInitialized) {
            cameraFragment = CameraFragment.newInstance()
        }

        if (curentFragment == cameraFragment) {
            return
        }
        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, cameraFragment, "Albums").commit()
        curentFragment = cameraFragment
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        resetToolbar()
        when (menuItem.itemId) {
            R.id.action_photos -> {
                moveToAllPhotos()
            }
            R.id.action_albums -> {
                moveToAlbums()
            }

            R.id.action_camera -> {
                moveToCamera()
            }
            R.id.nav_about -> {
                startAppActivity(this, AppInfoActivity::class.java)
                closeDrawer()
                return true
            }

        }
        return true
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort_date -> {
                sortBy = 1
                moveToDateWise()
                return true
            }
            R.id.menu_all_photos -> {
                sortBy = 0
                moveToAllPhotos()
                return true
            }
            R.id.menu_albums_list -> {
                sortBy = 0
                moveToListAlbums()
                return true
            }
            R.id.menu_grid -> {
                sortBy = 0
                moveToAlbums()
                return true
            }


            else -> super.onOptionsItemSelected(item)

        }
    }


    fun makeReset(photosAdapter: ListAdapter?,baseMovieAdapter:BaseMovieAdapter?, list: List<Album>) {

        for (item in list) {
            if (item.isSelected) {
                item.isSelected = !item.isSelected
            }
        }

       resetToolbar()
        if (photosFragment.getAdapter()!=null) {
            photosFragment.getAdapter().isSelection = false
            photosFragment.getAdapter().notifyDataSetChanged()
        }else if (baseMovieAdapter!=null){
            baseMovieAdapter.isSelection = false
            baseMovieAdapter.notifyDataSetChanged()
        }

    }

    private fun resetToolbar() {
        toolbar.visibility = View.VISIBLE
        toolbar_share_delete.visibility = View.GONE
        enableDisableScrollFlags(toolbar,false)
    }

    fun makeShareaDeleteToolbar(
        photosAdapter: ListAdapter?,
        baseAlbumsAdapter:BaseMovieAdapter?, list: List<Album>) {
        this.baseAlbumsAdapter = baseAlbumsAdapter
        var count = 0
        for (item in list) {
            if (item.isSelected) {
                count += 1
            }
        }
        txtSelectionCountToolbar.text = "" + count

        if (toolbar_share_delete.visibility == View.GONE) {
            toolbar_share_delete.navigationIcon =
                ContextCompat.getDrawable(applicationContext, R.drawable.ic_clear_white_24dp)
            toolbar.visibility = View.GONE
            toolbar_share_delete.visibility = View.VISIBLE
            enableDisableScrollFlags(toolbar,true)
            enableDisableScrollFlags(toolbar_share_delete,true)
            toolbar_share_delete.setNavigationOnClickListener {
                makeReset(photosAdapter,baseAlbumsAdapter, list)
            }

        }
    }
    private fun enableDisableScrollFlags(toolbar : Toolbar,flag :Boolean){
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        if (flag) {
            params.scrollFlags = 0
        }else{
            params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        }
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }


    private fun setNavigation() {
        navigation.setOnNavigationItemSelectedListener(this)
        nav_view.setNavigationItemSelectedListener(this)
        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehavior()
    }

    /*navigation bar Icons and drawer toogle*/
    internal fun setNavDrawer() {

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.drawerArrowDrawable.color = ContextCompat.getColor(context, R.color.colorWhite)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onBackPressed() {

        try {
            resetToolbar()
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                closeDrawer()
                return
            }
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showMsg(nav_view, "click BACK again to exit")
            vibrate(context)

            Handler().postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        } catch (e: Exception) {
            makeLog("Exception", e.localizedMessage)
        }
    }

}
