package apps.ranganathan.gallery.ui.activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import apps.ranganathan.gallery.BuildConfig
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.ui.fragment.AlbumsFragment
import apps.ranganathan.gallery.ui.fragment.MovieFragment
import apps.ranganathan.gallery.ui.fragment.PhotosFragment
import apps.ranganathan.gallery.utils.BottomNavigationBehavior
import apps.ranganathan.gallery.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var photoFile: File
    private val TAKE_PHOTO_REQUEST: Int = 12
    private lateinit var mCurrentPhotoPath: String
    private var sortBy: Int = 0
    private lateinit var homeVieModel: HomeViewModel
    private lateinit var albumsFragment: AlbumsFragment
    private lateinit var photosFragment: PhotosFragment
    private lateinit var movieFragment: MovieFragment
    private lateinit var curentFragment: Fragment
    private val DIRECTORY = "/GalleryImages"


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

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_photos -> {
                moveToAllPhotos()
            }
            R.id.action_albums -> {
                // initAlbum(homeVieModel.getAlbums(this@HomeActivity))
                moveToAlbums()
            }

            R.id.action_camera -> {
                onLaunchCamera()
             /*   val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE )
                startActivityForResult(camera, TAKE_PHOTO_REQUEST)*/

                /*takePhoto(object : ImagePickerListener {
                    override fun onCancelled() {
                        showToast("Camera cancelled")
                    }

                    override fun onPicked(bitmap: Bitmap) {
                        homeVieModel.saveImage(context, bitmap, DIRECTORY)

                    }
                })*/
            }
        }
        return true
    }

    public fun Savefile(name: String, path: String) {
        val direct = File(Environment.getExternalStorageDirectory(), "/MyAppFolder/MyApp/");
        val file = File(Environment.getExternalStorageDirectory(), "/MyAppFolder/MyApp/$name.png")

        if (!direct.exists()) {
            direct.mkdir()
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                val src = FileInputStream(path).channel
                val dst = FileOutputStream(file).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {

                if (photoFile!=null && ::photoFile.isInitialized)
                photoFile.createNewFile()

                /*val selectedImage = data!!.data
                val filePathColumn = { MediaStore.Images.Media.DATA }
                var projection: Array<String> = arrayOf(
                    MediaStore.Images.ImageColumns.DATA
                )


                val cursor = contentResolver.query(selectedImage, projection, null, null, null);
                cursor.moveToFirst()

                val columnIndex = cursor.getColumnIndex(projection[0]);
                //file path of captured image
                val filePath = cursor.getString(columnIndex)
                //file path of captured image
                val f = File(filePath)
                val filename = f.name

                Toast.makeText(getApplicationContext(), "Your Path:" + filePath, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Your Filename:" + filename, Toast.LENGTH_LONG).show();
                cursor.close()

                //Convert file path into bitmap image using below line.
                val yourSelectedImage = BitmapFactory.decodeFile(filePath);
                Toast.makeText(getApplicationContext(), "Your image" + yourSelectedImage, Toast.LENGTH_LONG).show()

                //put bitmapimage in your imageview
                //yourimgView.setImageBitmap(yourSelectedImage);

                Savefile(filename, filePath);*/
            }

        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri("photo")

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        val timeStamp = SimpleDateFormat("MMdd_HHmm").format(Date())
        val imageFileName = "Capture_$fileName $timeStamp.jpg"
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString() + DIRECTORY)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            makeLog("failed to create directory")
        }

        // Return the file target for the photo based on filename

        val path = mediaStorageDir.path + File.separator + imageFileName
        return File(path)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + DIRECTORY
        )

        // have the object build the directory structure, if needed.
        Log.w("save image", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        /* var image = File.createTempFile(
             imageFileName, *//* prefix *//*
            ".jpg", *//* suffix *//*
            wallpaperDirectory      *//* directory *//*
        )*/

        var image = File(
            wallpaperDirectory.path + File.separator +
                    "IMG_" + timeStamp + ".jpg"
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun launchCamera() {
        /* val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       *//*  val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)*//*
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + DIRECTORY
        )

        // have the object build the directory structure, if needed.
        Log.w("save image", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        val output = File(wallpaperDirectory, "CameraContentDemo.jpg")
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output))

        startActivityForResult(i, TAKE_PHOTO_REQUEST)*/


        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        values.put(MediaStore.Images.Media.SIZE, 2);
/*
        values.put(MediaStore.Images.Media.TITLE, "imageTitle");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "imageDisplayName");
        values.put(MediaStore.Images.Media.DESCRIPTION, "imageDescription");
        values.put(MediaStore.Images.Media.DATE_ADDED, "dateTaken");
        values.put(MediaStore.Images.Media.DATE_TAKEN, "dateTaken");
        values.put(MediaStore.Images.Media.DATE_MODIFIED, "dateTaken");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.ORIENTATION, 0);

        val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString()+ DIRECTORY)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            makeLog( "failed to create directory")
        }

        // Return the file target for the photo based on filename

        val path = mediaStorageDir.path + File.separator + "imageFileName.jpg"

        //val path = mediaStorageDir!!.toString().toLowerCase()
 val name = mediaStorageDir.getName().toLowerCase();
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, path.hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
        values.put(MediaStore.Images.Media.SIZE, mediaStorageDir.length());

        values.put(MediaStore.Images.Media.DATA, mediaStorageDir.getAbsolutePath());*/
        val fileUri = contentResolver
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )

        val result = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        takePictureIntent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
        /*val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")


       *//* val fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)*//*

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takePictureIntent.resolveActivity(packageManager) != null) {

            // Create the File where the photo should go
            lateinit  var photoFile: File
            try {
                photoFile = createImageFile()
            } catch (ex: IOException ) {
                // Error occurred while creating the File
                showMsg(toolbar,ex.localizedMessage)

            }
            // Continue only if the File was successfully created
            val photoURI = getUriForFile(this,
                BuildConfig.APPLICATION_ID+ ".provider",
                photoFile )
           *//* takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)*//*


            mCurrentPhotoPath = photoURI.toString()
            val file = Uri.fromFile(photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file)
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
        }*/
    }


    private fun setNavigation() {
        navigation.setOnNavigationItemSelectedListener(this)
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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


}
