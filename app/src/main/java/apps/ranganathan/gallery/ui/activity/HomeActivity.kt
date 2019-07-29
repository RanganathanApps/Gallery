package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
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
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.fragment.*
import apps.ranganathan.gallery.utils.BottomNavigationBehavior
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import kotlinx.android.synthetic.main.toolbar_home_share_delete.*
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    private var sortBy: Int = 0
    private var sortByDirectory: Int = 0
    private lateinit var homeVieModel: HomeViewModel
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var photosFragment: PhotosFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var photosDateOrderFragment: PhotosDateOrderFragment
    private lateinit var albumsListFragment: AlbumsListFragment
    private lateinit var curentFragment: Fragment

    private var doubleBackToExitPressedOnce = false

    var photosAdapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        //setConnectivityChange()
        initCode()
    }

    @Override
    fun initCode() {
        homeVieModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        setNavDrawer()
        setNavigation()
        logFcmToken()

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

            if (::photosFragment.isInitialized && curentFragment == photosFragment) {
                shareMultileFiles(photosFragment.adapter.listItems)
            }
            if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
                shareMultileFiles(cameraFragment.adapter.listItems)
            }
            if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
                shareMultileFiles(photosDateOrderFragment.getAdapter().listItems)
            }
            if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
                shareMultileFiles(albumsListFragment.getAdapter().listItems)
            }
            releaseSelections()

        }
        imgDeleteToolbar.setOnClickListener {
            deleteMultipleFiles()

        }
        checkBoxSelection.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.setOnClickListener {
                if (isChecked) {
                    selectAll()
                } else {
                    unSelectAll()
                }
            }

        }


    }

    private fun unSelectAll() {
        var list: List<Any> = arrayListOf()

        if (::photosFragment.isInitialized && curentFragment == photosFragment) {
            list = photosFragment.adapter.listItems
        }
        if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
            list = cameraFragment.adapter.listItems
        }
        if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
            list = photosDateOrderFragment.getAdapter().listItems
        }
        if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
            list = albumsListFragment.getAdapter().listItems
        }

        for (item in list) {
            item as Album
            if (item.isSelected) {
                item.isSelected = false
            }
        }

        if (::photosFragment.isInitialized && curentFragment == photosFragment) {
            photosFragment.getAdapter().notifyDataSetChanged()
        }
        if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
            cameraFragment.getAdapter().notifyDataSetChanged()
        }
        if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
            photosDateOrderFragment.getAdapter().notifyDataSetChanged()
        }
        if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
            albumsListFragment.getAdapter().notifyDataSetChanged()
        }
        txtSelectionCountToolbar.text = "0"
    }

    private fun selectAll() {
        var list: List<Any> = arrayListOf()

        var count = 0
        if (::photosFragment.isInitialized && curentFragment == photosFragment) {
            list = photosFragment.adapter.listItems
        }
        if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
            list = cameraFragment.adapter.listItems
        }
        if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
            list = photosDateOrderFragment.getAdapter().listItems
        }
        if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
            list = albumsListFragment.getAdapter().listItems
        }

        for (item in list) {
            item as Album
            if (!item.isSelected) {
                item.isSelected = !item.isSelected
            }
            if (!item.isSectionHeader)
                count += 1
        }

        if (::photosFragment.isInitialized && curentFragment == photosFragment) {
            photosFragment.getAdapter().notifyDataSetChanged()
        }
        if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
            cameraFragment.getAdapter().notifyDataSetChanged()
        }
        if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
            photosDateOrderFragment.getAdapter().notifyDataSetChanged()
        }
        if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
            albumsListFragment.getAdapter().notifyDataSetChanged()
        }
        txtSelectionCountToolbar.text = "" + count

    }

    private fun deleteMultipleFiles() {
        showConfirmationAlert(
            "Delete",
            "Image will be deleted permanently. do you want to continue?",
            object : Utils.OnClickListener {
                override fun onClick(v: View) {

                    var deletion = arrayListOf<Album>()
                    if (::photosFragment.isInitialized && curentFragment == photosFragment) {
                        deletion = photosFragment.deleteFile(this@HomeActivity)
                    }
                    if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
                        deletion =  cameraFragment.deleteFile(this@HomeActivity)
                    }
                    if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
                        deletion = photosDateOrderFragment.deleteFile(this@HomeActivity)
                    }
                    if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
                        deletion =   albumsListFragment.deleteFile(this@HomeActivity)
                    }

                    if (::photosFragment.isInitialized) {
                        deletion.forEachIndexed { index, album ->
                            photosFragment.adapter.listItems.forEachIndexed { index, any ->
                                if (album.albumUri.equals((any as Album).albumUri))
                                    (photosFragment.adapter.listItems[index] as Album).isSelected = true
                            }
                        }
                        photosFragment.adapter.deleteItems()
                    }
                    if (::cameraFragment.isInitialized) {
                        deletion.forEachIndexed { index, album ->
                            cameraFragment.adapter.listItems.forEachIndexed { index, any ->
                                if (album.albumUri.equals((any as Album).albumUri))
                                    (cameraFragment.adapter.listItems[index] as Album).isSelected = true
                            }
                        }
                        cameraFragment.adapter.deleteItems()
                    }
                    if (::photosDateOrderFragment.isInitialized) {
                        deletion.forEachIndexed { index, album ->
                            photosDateOrderFragment.adapter.listItems.forEachIndexed { index, any ->
                                if (album.albumUri.equals((any as Album).albumUri))
                                    (photosDateOrderFragment.adapter.listItems[index] as Album).isSelected = true
                            }
                        }
                        photosDateOrderFragment.adapter.deleteItems()
                    }
                    if (::albumsListFragment.isInitialized) {
                        deletion.forEachIndexed { index, album ->
                            albumsListFragment.adapter.listItems.forEachIndexed { index, any ->
                                if (album.albumUri.equals((any as Album).albumUri))
                                    (albumsListFragment.adapter.listItems[index] as Album).isSelected = true
                            }
                        }
                        albumsListFragment.getAdapter().deleteItems()
                    }
                    releaseSelections()

                }
            },
            object : Utils.OnClickListener {
                override fun onClick(v: View) {
                }
            })

    }


    @Override
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    var bundle = data!!.extras
                    var list = bundle.getSerializable("deleted_albums")
                    removeItems(list)
                }
            }
        }
        try {
            super.onActivityResult(requestCode, resultCode, data)
        } catch (e: Exception) {
        }
    }

    private fun removeItems(data: Any) {
        data as MutableList<Album>
        if (data.isNotEmpty()) {

        }

        if (::photosFragment.isInitialized) {
            data.forEachIndexed { index, album ->
                val item = photosFragment.adapter.listItems.find { (it as Album).albumUri.equals(album.albumUri) }
                if (item != null)
                    (item as Album).isSelected = true
            }
            photosFragment.adapter.deleteItems()
        }
        if (::cameraFragment.isInitialized) {
            data.forEachIndexed { index, album ->
                val item = cameraFragment.adapter.listItems.find { (it as Album).albumUri.equals(album.albumUri) }
                if (item != null)
                    (item as Album).isSelected = true
            }
            cameraFragment.adapter.deleteItems()
        }
        if (::photosDateOrderFragment.isInitialized) {
            data.forEachIndexed { index, album ->
                val item =
                    photosDateOrderFragment.adapter.listItems.find { (it as Album).albumUri.equals(album.albumUri) }
                val pos = photosDateOrderFragment.adapter.listItems.indexOf(item)
                if ((item as Album).isSectionHeader) {
                    if (item.dateString.equals((photosDateOrderFragment.adapter.listItems[pos + 2] as Album).dateString)) {
                        (photosDateOrderFragment.adapter.listItems[pos + 1] as Album).isSelected = true
                    } else {
                        (item as Album).isSelected = true
                        (photosDateOrderFragment.adapter.listItems[pos + 1] as Album).isSelected = true
                    }
                } else {
                    (item as Album).isSelected = true
                }
            }
            photosDateOrderFragment.adapter.deleteItems()
        }
        if (::albumsListFragment.isInitialized) {
            data.forEachIndexed { index, album ->
                val item = albumsListFragment.adapter.listItems.find { (it as Album).albumUri.equals(album.albumUri) }
                if (item != null)
                    (item as Album).isSelected = true
                if ((item as Album).isSectionHeader) {
                    val pos = albumsListFragment.adapter.listItems.indexOf(item)
                    (albumsListFragment.adapter.listItems[pos + 1] as Album).isSelected = true
                }
            }
            albumsListFragment.getAdapter().deleteItems()
        }

    }

    private fun releaseSelections() {
        if (::photosFragment.isInitialized && curentFragment == photosFragment) {
            makeReset(photosFragment.getAdapter().listItems)
        }
        if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
            makeReset(cameraFragment.getAdapter().listItems)
        }
        if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
            makeReset(photosDateOrderFragment.getAdapter().listItems)
        }
        if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
            makeReset(albumsListFragment.getAdapter().listItems)
        }
        resetToolbar()
    }


    private fun shareMultileFiles(list: List<Any>) {
        var uris = ArrayList<Uri>()

        for (item in list) {
            item as Album

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


        val receiver = Intent(this, ApplicationSelectorReceiver::class.java);
        val pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val chooser = Intent.createChooser(shareIntent, null, pendingIntent.intentSender)
            startActivity(chooser);
        } else {
            startActivity(Intent.createChooser(shareIntent, "Share using"))
        };


    }

    class ApplicationSelectorReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun onReceive(context: Context, intent: Intent) {
            for (key in Objects.requireNonNull(intent.extras).keySet()) {
                try {
                    val componentInfo = intent.extras!!.get(key) as ComponentName
                    val packageManager = context.packageManager
                    val appName = packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(
                            componentInfo.packageName,
                            PackageManager.GET_META_DATA
                        )
                    ) as String
                    Log.w("Selected Name", appName)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
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
        if (sortByDirectory == 1) {
            moveToListAlbums()
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
        releaseSelections()
        closeDrawer()
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
                return true
            }
            R.id.nav_help_feedback -> {
                startAppActivity(this, HelpFeedbackActivity::class.java)
                return true
            }
            R.id.nav_rate_us -> {
                startAppActivity(this, RateAppActivity::class.java)
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
                sortByDirectory = 1
                moveToListAlbums()
                return true
            }
            R.id.menu_grid -> {
                sortByDirectory = 0
                moveToAlbums()
                return true
            }


            else -> super.onOptionsItemSelected(item)

        }
    }


    fun makeReset(list: List<Any>) {

        for (item in list) {
            item as Album
            if (item.isSelected) {
                item.isSelected = !item.isSelected
            }
        }

        resetToolbar()
        if (photosFragment.getAdapter() != null) {
            photosFragment.getAdapter().isSelection = false
            photosFragment.getAdapter().notifyDataSetChanged()
        }
        if (::cameraFragment.isInitialized && curentFragment == cameraFragment) {
            cameraFragment.getAdapter().isSelection = false
            cameraFragment.getAdapter().notifyDataSetChanged()
        }
        if (::photosDateOrderFragment.isInitialized && curentFragment == photosDateOrderFragment) {
            photosDateOrderFragment.getAdapter().isSelection = false
            photosDateOrderFragment.getAdapter().notifyDataSetChanged()
        }
        if (::albumsListFragment.isInitialized && curentFragment == albumsListFragment) {
            albumsListFragment.getAdapter().isSelection = false
            albumsListFragment.getAdapter().notifyDataSetChanged()
        }

    }

    private fun resetToolbar() {
        toolbar.visibility = View.VISIBLE
        toolbar_share_delete.visibility = View.GONE
        enableDisableScrollFlags(toolbar, false)
    }

    fun makeShareDeleteToolbar(list: List<Any>) {
        var count = 0
        for (item in list) {
            item as Album
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
            enableDisableScrollFlags(toolbar, true)
            enableDisableScrollFlags(toolbar_share_delete, true)
            toolbar_share_delete.setNavigationOnClickListener {
                makeReset(list)
            }

        }
        checkBoxSelection.isChecked = false

    }

    private fun enableDisableScrollFlags(toolbar: Toolbar, flag: Boolean) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        if (flag) {
            params.scrollFlags = 0
        } else {
            params.scrollFlags =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
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
            releaseSelections()
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                closeDrawer()
                return
            }
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showMsg(nav_view, "Press BACK again to exit!")
            vibrate(context)

            Handler().postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        } catch (e: Exception) {
            makeLog("Exception", e.localizedMessage)
        }
    }


    fun logFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    makeLog("FCM token getInstanceId failed" + task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                makeLog("FCM token: $token")
            })
    }

}
