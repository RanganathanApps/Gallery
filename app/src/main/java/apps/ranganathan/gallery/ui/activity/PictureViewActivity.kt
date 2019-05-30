package apps.ranganathan.gallery.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ViewPagerAdapter
import apps.ranganathan.gallery.model.Album
import kotlinx.android.synthetic.main.content_picture_view.*
import java.io.File
import androidx.viewpager.widget.ViewPager
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import android.R.attr.bitmap
import android.view.Menu
import android.view.MenuItem
import apps.ranganathan.gallery.viewmodel.PictureViewModel


class PictureViewActivity : BaseActivity() {

    private lateinit var pictureViewModel: PictureViewModel
    lateinit var userList: List<Album>
    private var position: Int = 0


    private lateinit var directory: String
    private lateinit var count: String

    private lateinit var files: ArrayList<File>

    private lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_view)
        setAppBar("")

        pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel::class.java)
        if (intent!!.extras != null) {
            if (intent!!.extras!!.containsKey("album")) {
                album = intent!!.extras!!.getSerializable("album") as Album
                directory = album.name
                count = album.count
                setToolBarTitle("$directory (${1}/${album.count} items)")
                files = pictureViewModel.getImagesInFile(pictureViewModel.getDirectory(album.path))
                userList = pictureViewModel.getImages(files)
            } else {
                userList = intent!!.extras!!.getSerializable("photos") as List<Album>
                position = intent!!.extras!!.getInt("position")
                album = userList[position]
                setToolBarTitle(album.name)
            }

            //pictureViewModel.position.observe(this, Observer {    setAppBar("$directory (${viewpagerPhotos.currentItem} / ${album.count} items)") })

            viewpagerPhotos.adapter = ViewPagerAdapter(this, userList,pictureViewModel.position)
            viewpagerPhotos.setCurrentItem(position, true)

            viewpagerPhotos.addOnPageChangeListener (object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(i: Int, v: Float, i2: Int) {
                    //Toast.makeText(MyActivity.this, i+"  Is Selected  "+data.size(), Toast.LENGTH_SHORT).show();
                }

                override fun onPageSelected(i: Int) {
                    // here you will get the position of selected page
                    album = userList[i]
                    try {
                        setToolBarTitle("$directory (${i+1}/${userList.size} items)")
                    } catch (e: UninitializedPropertyAccessException) {

                    }

                }

                override fun onPageScrollStateChanged(i: Int) {

                }
            })

        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_picture_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_info -> {
                val map = mapOf("album" to album)
                startActivityputExtra(this,InfoActivity::class.java, map)
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }

}
