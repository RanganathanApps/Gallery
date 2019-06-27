package apps.ranganathan.gallery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import apps.ranganathan.configlibrary.base.BaseAppActivity
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import com.github.chrisbanes.photoview.PhotoViewAttacher
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent




class ViewPagerAdapter(
    private val context: Context,
    list: MutableList<Album>,
    position1: MutableLiveData<Int>,
    touchToggle: MutableLiveData<Boolean>
) : PagerAdapter() {
    private var layoutInflater : LayoutInflater? = null
    private var imgaeslist = list
    private var position1 = position1
    private var touchToggle = touchToggle



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ===  `object`
    }

    override fun getCount(): Int {
        return imgaeslist.size
    }

    fun updateList(updatedList : MutableList<Album>) {
        imgaeslist = updatedList
    }


    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.item_picture_view , null)
        val imgAlbum = v.findViewById<View>(R.id.imgAlbum) as ImageView
        position1.value = position

        val activity :BaseAppActivity = context as BaseAppActivity
        activity.loadImage(imgaeslist[position].albumUri,
            imgAlbum,
            R.drawable.ic_camera_alt_white_24dp)

        imgAlbum.setOnClickListener {
            touchToggle.value = !touchToggle.value!!

        }
       /* imgAlbum.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                touchToggle.value = !touchToggle.value!!
                return true
            }
        })*/

        val vp = container as ViewPager
        vp.addView(v , 0)


        return v

    }



    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }



}