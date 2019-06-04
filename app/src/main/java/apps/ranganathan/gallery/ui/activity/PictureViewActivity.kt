package apps.ranganathan.gallery.ui.activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ViewPagerAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.viewmodel.PictureViewModel
import kotlinx.android.synthetic.main.activity_picture_view.*
import kotlinx.android.synthetic.main.content_picture_view.*
import java.io.File


class PictureViewActivity : BaseActivity() {


    private lateinit var pictureViewModel: PictureViewModel
    lateinit var userList: List<Album>
    private var position: Int = 0


    private lateinit var directory: String
    private lateinit var count: String

    private lateinit var files: ArrayList<File>

    private lateinit var album: Album

    private var touchToggle = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_view)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setAppBar("")

        changeToolbarNavIconColor(R.color.colorWhite)

        touchToggle.value = false



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
        }

        touchToggle.observe(this, Observer {
            it
            if (it) {
                showToolbar()
            } else {
                hideToolbar()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decor = window.decorView
                if (touchToggle.value!!) {
                   /* decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    window.statusBarColor = Color.RED
                    window.setStatusBarColor( Color.RED);*/
                    /*enable status bar here*/
                    //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    setToolBarHeight()
                } else {

                    /*hide status bar here*/
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    window.statusBarColor = Color.RED
                }
            }
        })
        setUpViewPager()

    }

    private fun setToolBarHeight() {
        val statusBarHeight = getStatusBarHeight()
                    //action bar height
                    setMargins(appBar,0,statusBarHeight,0,0)
    }




    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }



    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }





    private fun setUpViewPager() {

        viewpagerPhotos.adapter = ViewPagerAdapter(this, userList, pictureViewModel.position, touchToggle)
        viewpagerPhotos.setCurrentItem(position, true)

        viewpagerPhotos.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i2: Int) {
                //Toast.makeText(MyActivity.this, i+"  Is Selected  "+data.size(), Toast.LENGTH_SHORT).show();
            }

            override fun onPageSelected(i: Int) {
                // here you will get the position of selected page
                album = userList[i]
                try {
                    setToolBarTitle("$directory (${i + 1}/${userList.size} items)")
                } catch (e: UninitializedPropertyAccessException) {
                    setToolBarTitle(userList[i].name)
                } catch (e: Exception) {
                    setToolBarTitle(userList[i].name)
                }

            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

    }

    private fun hideToolbar() {
        appBar.visibility = GONE
        //setStatusBarVisibility(false)
         /* window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
*/


    }



    private fun showToolbar() {
        appBar.visibility = VISIBLE
        //setStatusBarVisibility(true)
         // window!!.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

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
                startActivityputExtra(this, InfoActivity::class.java, map)
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }

}
