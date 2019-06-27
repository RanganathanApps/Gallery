package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.ui.fragment.AlbumsFragment
import apps.ranganathan.gallery.ui.fragment.CameraFragment
import apps.ranganathan.gallery.ui.fragment.MovieFragment
import apps.ranganathan.gallery.ui.fragment.PhotosFragment
import apps.ranganathan.gallery.utils.BottomNavigationBehavior
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*


class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    private var sortBy: Int = 0
    private lateinit var homeVieModel: HomeViewModel
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var photosFragment: PhotosFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var movieFragment: MovieFragment
    private lateinit var curentFragment: Fragment

    private var doubleBackToExitPressedOnce = false

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


    }

    private fun moveToDateWise() {
        sortBy = 1

        if (!::movieFragment.isInitialized) {
            movieFragment = MovieFragment()

        }

        if (curentFragment == movieFragment) {
            return
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, movieFragment, "Photos").commit()
        curentFragment = movieFragment
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
    private fun setNavDrawer() {

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_photos -> {
                sortBy = 1
                moveToDateWise()
                return true
            }
            R.id.menu_all_photos -> {
                sortBy = 0
                moveToAllPhotos()
                return true
            }


            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onBackPressed() {
        try {
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
            makeLog("Exception",e.localizedMessage)
        }
    }


}
