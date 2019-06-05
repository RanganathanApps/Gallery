package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import apps.ranganathan.gallery.ui.fragment.AlbumsFragment
import apps.ranganathan.gallery.ui.fragment.PhotosFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class HomeActivity : BaseActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var photosFragment: PhotosFragment
    private  val DIRECTORY = "/GalleryImages"

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
                if (!::photosFragment.isInitialized) {
                    albumsFragment = AlbumsFragment.newInstance()
                }

                supportFragmentManager.beginTransaction().replace(R.id.frameFragmentHolder, albumsFragment, "Albums").commit()
            }
            R.id.action_favourites ->{

            }
            R.id.action_camera ->{
                takePhoto(object : ImagePickerListener{
                    override fun onCancelled() {

                    }

                    override fun onPicked(bitmap: Bitmap) {
                        saveImage(bitmap)

                    }
                })
            }
        }
        return true
    }

    fun saveImage(myBitmap: Bitmap): String {


        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.w("save image", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.w("save", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.w("save ", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
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

      /*  val glm = GridLayoutManager(this, 3)
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

        recyclerAlbums.layoutManager = GridLayoutManager(this,  4) as RecyclerView.LayoutManager?
        //recyclerAlbums.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //now adding the adapter to recyclerview
        recyclerAlbums.setHasFixedSize(true)
        recyclerAlbums.adapter = adapter
        //recyclerAlbums.setItemViewCacheSize(20)
        //recyclerAlbums.setDrawingCacheEnabled(true)


    }





    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0f)
    }
    private fun initAlbum(files: List<Album>) {

        recyclerAlbums.layoutManager = GridLayoutManager(this,  3) as RecyclerView.LayoutManager?
        //recyclerAlbums.layoutManager = StaggeredGridLayoutManager(2,VERTICAL)

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
