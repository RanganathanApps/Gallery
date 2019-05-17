package apps.ranganathan.gallery

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.adapter.CustomAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.HomeViewModel

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.io.File
import kotlin.math.absoluteValue



class HomeActivity : BaseAppActivity() {

    private lateinit var homeVieModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        setConnectivityChange()


        homeVieModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var files = homeVieModel.getAllImages(this)
        initAlbum(files)

        fabCamera.setOnClickListener { view ->
           takePhoto(object : ImagePickerListener{
               override fun onPicked(bitmap: Bitmap) {
                   showToast(""+bitmap.height.toString())
               }
           })
        }

    }

    private lateinit var albumm: Album

    private fun initAlbum(files: List<File>) {

        recyclerAlbums.layoutManager =  GridLayoutManager(this,  2)
        val users = ArrayList<Album>()

        for (file in files){
            // i in [1, 10), 10 is excluded
            albumm = Album()
            albumm.name = file.nameWithoutExtension
            var k = Uri.fromFile(file)
            val parent = File(file.parent)
            albumm.count  = parent.nameWithoutExtension
            albumm.albumUri = k.toString()
            users.add(albumm)
        }



        val adapter = CustomAdapter(this,users)

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
            R.id.action_album -> {
                var files = homeVieModel.getAlbums(this)
                initAlbum(files)
                true
            }
            R.id.action_photos -> {
                var files = homeVieModel.getAllImages(this)
                initAlbum(files)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
}
