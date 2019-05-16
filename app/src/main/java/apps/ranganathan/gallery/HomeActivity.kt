package apps.ranganathan.gallery

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.adapter.CustomAdapter
import apps.ranganathan.gallery.model.Album

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : BaseAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        setConnectivityChange()
        initAlbum()
        fabCamera.setOnClickListener { view ->
           takePhoto(object : ImagePickerListener{
               override fun onPicked(bitmap: Bitmap) {
                   showToast(""+bitmap.height.toString())
               }
           })
        }

    }

    private lateinit var albumm: Album

    private fun initAlbum() {

        recyclerAlbums.layoutManager =  GridLayoutManager(this,  2)
        val users = ArrayList<Album>()

        //adding some dummy data to the list
        for (i in 1 until 10) {
            // i in [1, 10), 10 is excluded
            println(i)
            albumm = Album()
            albumm.name = "abc"+i*i
            albumm.count = ""+i*i
            albumm.albumUri = ""
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
