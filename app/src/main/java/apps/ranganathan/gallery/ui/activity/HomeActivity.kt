package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_drawer.*

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import apps.ranganathan.gallery.ui.fragment.AlbumsFragment
import apps.ranganathan.gallery.ui.fragment.MovieFragment
import apps.ranganathan.gallery.ui.fragment.PhotosFragment
import apps.ranganathan.gallery.utils.BottomNavigationBehavior


class HomeActivity : BaseActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var homeVieModel: HomeViewModel
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var photosFragment: PhotosFragment
    private lateinit var movieFragment: MovieFragment
    private  val DIRECTORY = "/GalleryImages"



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

        getPermission(this,object :PermissionListener{
            override fun onDenied(deniedPermissions: List<String>) {
                showMsg(navigation,"Permissions are required to run this app")
            }

            override fun onDeniedForeEver(deniedPermissions: List<String>) {
                showMsg(navigation,"Enable Permissions on settings")
            }

            override fun onGranted() {
                movieFragment = MovieFragment()
                if (!::photosFragment.isInitialized) {
                    photosFragment = PhotosFragment.newInstance()
                }
                supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, movieFragment, "Photos").commit()
            }
        },Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_photos ->{
                if (!::photosFragment.isInitialized) {
                    photosFragment = PhotosFragment.newInstance()
                }
                supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, photosFragment, "Photos").commit()
                //initPhotos(homeVieModel.getAllImages(this@HomeActivity))
            }
            R.id.action_albums ->{
                // initAlbum(homeVieModel.getAlbums(this@HomeActivity))
                if (!::albumsFragment.isInitialized) {
                    albumsFragment = AlbumsFragment.newInstance()
                }

                supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, albumsFragment, "Albums").commit()
            }

            R.id.action_camera ->{

                takePhoto(object : ImagePickerListener{
                    override fun onCancelled() {
                        showToast("Camera cancelled")
                    }

                    override fun onPicked(bitmap: Bitmap) {
                        homeVieModel.saveImage(context,bitmap,DIRECTORY)

                    }
                })
            }
        }
        return true
    }

    private fun setNavigation() {
        navigation.setOnNavigationItemSelectedListener(this)
        val layoutParams =  navigation.layoutParams as CoordinatorLayout.LayoutParams
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            else -> super.onOptionsItemSelected(item)

        }
    }



}
