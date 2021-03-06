package apps.ranganathan.gallery.ui.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import apps.ranganathan.configlibrary.utils.Utils
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ViewPagerAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.utils.BottomNavigationBehavior
import apps.ranganathan.gallery.viewmodel.PictureViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_picture_view.*
import kotlinx.android.synthetic.main.content_picture_view.*
import kotlinx.android.synthetic.main.toolbar_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.Serializable
import java.util.*


class PictureViewActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    private lateinit var id: String
    private lateinit var adapter: ViewPagerAdapter
    private var slideShowShecduled: Boolean = false
    private lateinit var runnableUpdate: Runnable
    private lateinit var runnableAutoHide: Runnable
    private lateinit var handler: Handler
    private lateinit var handlerAutoHide: Handler
    private lateinit var timer: Timer
    private val TAG = "App"

    private lateinit var pictureViewModel: PictureViewModel
    lateinit var userList: MutableList<Album>
    private var position: Int = 0


    private lateinit var directory: String
    private lateinit var count: String

    private lateinit var files: ArrayList<File>

    private lateinit var album: Album

    var deletedList = arrayListOf<Any>()

    private var touchToggle = MutableLiveData<Boolean>()

    lateinit var progressBar: ProgressBar


    /*interface for data binding*/
    interface DeleteListener<T> {
        fun onDeleted(index: Int, data: T)
    }


    override fun onBackPressed() {
        var bundle = Bundle()
        bundle.putSerializable("deleted_albums", deletedList as Serializable)
        var intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_view)
        setAppBar("")
        changeToolbarNavIconColor(R.color.colorWhite)
        val mActionBar: ActionBar? = supportActionBar
        mActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorTrans)))
        toolbar.background.alpha = 0
        iniCode()

    }


    private fun iniCode() {
        pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel::class.java)

        if (intent != null) {
            val action = intent.action
            if (action != null && action.compareTo(Intent.ACTION_VIEW) == 0) {
                val scheme = intent!!.scheme
                if (scheme!!.compareTo(ContentResolver.SCHEME_CONTENT) == 0 ||
                    scheme.compareTo(ContentResolver.SCHEME_FILE) == 0) {
                    val uri = intent.data
                    val name = uri.lastPathSegment

                    makeLog(name)
                    userList = arrayListOf()
                    album = Album()
                    val size = uri.pathSegments.size - 1
                    var pathDirectory = StringBuffer()
                    for (i in 0 until size) {
                        pathDirectory.append("/")
                        pathDirectory.append(uri.pathSegments[i])

                    }
                    Log.w("view :", "filePath  :$pathDirectory")
                    Log.w("view :", "uri.encodedPath  :${uri.encodedPath}")
                    Log.w("view :", "uri  :${getRealPathFromURI(uri)}")
                    album.albumUri = uri.toString()
                    album.name = name
                    album.file = File(album.albumUri)
                    userList.add(0, album)
                    pictureViewModel.position.value = 0
                    setUpViewPager()
                } else {

                }
            } else if (intent!!.extras != null) {
                setNavigation()

                album = intent!!.extras!!.getSerializable("album") as Album
                progressCircularViewPager.visibility = VISIBLE

                if (!intent!!.extras!!.containsKey("tag")) {
                    return
                }
                if (intent!!.extras!!.getString("tag") == "album") {
                    album = intent!!.extras!!.getSerializable("album") as Album
                    directory = album.name
                    count = album.count
                    setToolBarTitle("$directory (${1}/${album.count} items)")
                    doAsync {
                        files = pictureViewModel.getImagesInFile(pictureViewModel.getDirectory(album.path))!!
                        userList = pictureViewModel.getImages(files)
                        uiThread {
                            setUpViewPager()
                        }
                    }


                } else if (intent!!.extras!!.getString("tag") == "photos") {

                    doAsync {
                        position = intent!!.extras!!.getInt("position")
                        album = intent!!.extras!!.getSerializable("album") as Album
                        userList = pictureViewModel.getAllImages(context)
                        uiThread {
                            setUpViewPager()
                            setToolBarTitle(album.name)
                        }
                    }

                } else if (intent!!.extras!!.getString("tag") == "camera") {
                    directory = intent!!.extras!!.getString("directory")
                    position = intent!!.extras!!.getInt("position")
                    doAsync {
                        files = pictureViewModel.getImagesInFile(pictureViewModel.getDirectory(directory))!!
                        directory = intent!!.extras!!.getString("directory_ui")
                        userList = pictureViewModel.getImages(files)
                        album = userList[position]
                        uiThread {
                            setUpViewPager()
                            setToolBarTitle("$directory (${1}/${userList.size} items)")
                        }
                    }


                } else if (intent!!.extras!!.getString("tag") == "albums_list") {
                    album = intent!!.extras!!.getSerializable("album") as Album
                    /*  files = pictureViewModel.getImagesInFile(pictureViewModel.getDirectory(album.path))!!
                  userList = pictureViewModel.getImages(files)*/
                    directory = album.bucket
                    doAsync {
                        userList = pictureViewModel.getSpecificFolderImages(context, album)
                        position = 0
                        userList.forEachIndexed { index, element ->
                            if (element.albumUri == (intent!!.extras!!.getSerializable("album") as Album).albumUri) {
                                position = index
                            }
                        }

                        uiThread {
                            if (userList.isNotEmpty()) {
                                album = userList[position]
                            }
                            pictureViewModel.position.value = position
                            setToolBarTitle("$directory (${1}/${userList.size} items)")
                            setUpViewPager()
                        }
                    }

                } else if (intent!!.extras!!.getString("tag") == "date") {
                    doAsync {
                        album = intent!!.extras!!.getSerializable("album") as Album
                        userList = pictureViewModel.getSpecificDateImages(context, album)
                        position = 0
                        position = userList.indexOfFirst { it.albumUri == album.albumUri } // -1 if not found
                        album = userList[position]
                        uiThread {
                            pictureViewModel.position.value = position
                            setUpViewPager()
                            setToolBarTitle("${album.dateString} (${1}/${userList.size} items)")
                        }
                    }

                }
                deletedList = arrayListOf()
            }
        }



        touchToggle.observe(this, Observer {
            if (it) {
                showToolbar()
            } else {
                hideToolbar()
            }

        })




    }


    public fun getRealPathFromURI(contentUri: Uri): String {
        try {
            val projection = arrayOf(
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA
            )


            val orderBy = MediaStore.Images.Media._ID
            makeLog("contentUri $contentUri")
            makeLog("FilecontentUri ${File(contentUri.path)}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (DocumentsContract.isDocumentUri(context.getApplicationContext(), contentUri)) {
                    val wholeID =  DocumentsContract.getDocumentId(contentUri)
                    // Split at colon, use second item in the array
                    id = wholeID.split(":")[1]
                    makeLog("$wholeID $id")

                }
            }

            val cursor = context.contentResolver.query(
                contentUri, projection,
                null, arrayOf(id), "$orderBy DESC"
            )


            while (cursor.moveToNext()) {
                val absolutePathOfImage = cursor.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                makeLog("absolutePathOfImage ${absolutePathOfImage}")
                val file = File(absolutePathOfImage)
            }

            // val cursor = managedQuery(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } catch (e: Exception) {
            makeLog("Exception ${e.localizedMessage}")
        }

        return  ""
    }

    private fun setAs(uri: Uri) {
        val intent = Intent(Intent.ACTION_ATTACH_DATA)
            .apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                setDataAndType(uri, "image/*")
                putExtra("mimeType", "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        startActivity(Intent.createChooser(intent, "Set as:"))
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        stopSlideShow()
        when (menuItem.itemId) {
            R.id.action_delete -> {
                showConfirmationAlert(
                    "Delete",
                    "Image will be deleted permanently. do you want to continue?",
                    object : Utils.OnClickListener {
                        override fun onClick(v: View) {
                            //deletedList.add(album)
                            val isDeleted = pictureViewModel.delete(context, album.file)
                            if (isDeleted) {
                                deletedList.add(album)
                                showToast("File Deleted!")
                                userList.removeAt(position)
                                adapter.updateList(userList)
                                adapter.notifyDataSetChanged()
                                viewpagerPhotos.adapter = adapter
                                viewpagerPhotos.adapter!!.notifyDataSetChanged()
                                //(lister as DeleteListener<Album>).onDeleted(0,album)
                                if (userList.size > 0) {
                                    if (position + 1 < userList.size) {
                                        position += 1
                                    } else {
                                        position = 0
                                    }
                                    viewpagerPhotos.currentItem = position
                                } else {
                                    onBackPressed()
                                }


                                // pictureViewModel.setMediaMounted(context, userList[position].file.absolutePath)

                            }
                        }

                    },
                    object : Utils.OnClickListener {
                        override fun onClick(v: View) {
                        }

                    })

            }
            R.id.action_share -> {
                val apkURI = FileProvider.getUriForFile(
                    context,
                    context.applicationContext
                        .packageName + ".provider", album.file
                )
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, apkURI)
                startActivity(Intent.createChooser(shareIntent, "Share image using"))
            }

            R.id.action_edit -> {
                val apkURI = FileProvider.getUriForFile(
                    context,
                    context.applicationContext
                        .packageName + ".provider", album.file
                )

                /* val install = Intent(Intent.ACTION_EDIT)
                 install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

 // New Approach

                 install.setDataAndType(apkURI, mimeType.toString())
                 install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
 // End New Approach
                 context.startActivity(install)*/
                val editIntent = Intent(Intent.ACTION_EDIT)
                editIntent.setDataAndType(apkURI, "image/*")
                editIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                startActivity(Intent.createChooser(editIntent, null))
                /* val map = mapOf("album" to album)
                 startActivityputExtra(this, EditActivity::class.java, map)*/
            }
            R.id.action_info -> {
                val map = mapOf("album" to album)
                startActivityputExtra(this, InfoActivity::class.java, map)
            }


        }
        return true
    }

    private fun setNavigation() {
        navigation.setOnNavigationItemSelectedListener(this)
        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehavior()
        navigation.menu.getItem(0).isCheckable = false
    }


    // This snippet hides the system bars.
    private fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


    private fun showToolbar() {
        appBar.visibility = VISIBLE
        val action = intent.action
        if (action != null && action.compareTo(Intent.ACTION_VIEW) == 0) {
            navigation.visibility = GONE
        }else {
            navigation.visibility = VISIBLE
        }

        setBootomBarHeight()
        showSystemUI()
        setToolBarHeight()
        stopSlideShow()
    }

    private fun stopSlideShow() {
        if (::timer.isInitialized && slideShowShecduled) {
            timer.cancel()
            slideShowShecduled = false
        }
    }


    private fun hideToolbar() {
        appBar.visibility = GONE
        navigation.visibility = GONE
        //hideSystemUISettings()
        hideSystemUI()
    }

    fun hasNavBar(resources: Resources): Boolean {
        val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        var res = id > 0 && resources.getBoolean(id)
        return res
    }


    fun getNavBarHeight(c: Context): Int {
        val result = 0

        if (!hasNavBar(c.resources)) {
            return result
        }

        if (hasNavBar(c.resources)) {
            //The device has a navigation bar
            val resources = c.resources

            val orientation = resources.configuration.orientation
            val resourceId: Int

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                resourceId = resources.getIdentifier(
                    "navigation_bar_height",
                    "dimen",
                    "android"
                )
            } else {
                resourceId = 0
            }


            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    private fun setToolBarHeight() {
        val statusBarHeight = getStatusBarHeight()
        //action bar height
        setMargins(appBar, 0, statusBarHeight, 0, 0)
    }

    private fun setBootomBarHeight() {
        val navigationBarHeight = getNavBarHeight(context)
        //action bar height
        setMargins(navigation, 0, 0, 0, navigationBarHeight)
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


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            showToolbar()
            touchToggle.value = true

        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            setMargins(navigation, 0, 0, 0, 0)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setBootomBarHeight()
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpViewPager() {
        progressCircularViewPager.visibility = GONE
        touchToggle.value = false
        adapter = ViewPagerAdapter(this, userList, pictureViewModel.position, touchToggle)
        viewpagerPhotos.adapter = adapter
        viewpagerPhotos.setCurrentItem(position, true)

        viewpagerPhotos.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i2: Int) {
                position = i
                album = userList[i]
                try {
                    when (intent!!.extras!!.getString("tag")) {
                        "date" -> setToolBarTitle("${album.dateString} (${i + 1}/${userList.size} items)")
                        "albums_list" -> setToolBarTitle("$directory (${i + 1}/${userList.size} items)")
                        else -> setToolBarTitle(userList[i].name)
                    }
                } catch (e: UninitializedPropertyAccessException) {
                    setToolBarTitle(userList[i].name)
                } catch (e: Exception) {
                    setToolBarTitle(userList[i].name)
                }
            }

            override fun onPageSelected(i: Int) {
                // here you will get the position of selected page


            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })
        handler = Handler()
        runnableUpdate = Runnable {
            if (position == userList.size) {
                position = 0
                stopSlideShow()
            }
            viewpagerPhotos.setCurrentItem(position++, true)
        }


        handlerAutoHide = Handler()
        runnableAutoHide = Runnable {
            /*initially hiding toolbar and bottom bar*/
            touchToggle.value = false
            hideToolbar()
        }
        handlerAutoHide.postDelayed(runnableAutoHide, 1000)


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_picture_view, menu)

        if (intent != null) {
            val action = intent.action
            if (action != null && action.compareTo(Intent.ACTION_VIEW) == 0) {
                menu!!.findItem(R.id.action_menu_slideshow).isVisible = false
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_set_as -> {
                val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", album.file);
                setAs(uri)
                true
            }
            R.id.action_menu_slideshow -> {
                timer = Timer() // This will create a new Thread
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        handler.post(runnableUpdate)
                        slideShowShecduled = true
                    } // task to be scheduled

                }, 1000, 2000)
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }


}
