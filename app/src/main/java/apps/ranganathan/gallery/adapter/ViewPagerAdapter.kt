package apps.ranganathan.gallery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ViewPagerAdapter(private val context : Context,list: List<Album>) : PagerAdapter() {
    private var layoutInflater : LayoutInflater? = null
    private var imgaeslist = list


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ===  `object`
    }

    override fun getCount(): Int {
        return imgaeslist.size
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.item_picture_view , null)
        val imgAlbum = v.findViewById<View>(R.id.imgAlbum) as ImageView
        Picasso.get().load(imgaeslist[position].albumUri).into(imgAlbum, object : Callback {
            override fun onError(e: Exception?) {

            }

            override fun onSuccess() {
                //imageAlbum.heightRatio = (imageAlbum.height /4).toDouble()
                //imageAlbum.layoutParams.height = imageAlbum.height/2
                //imageAlbum.layoutParams.width = imageAlbum.width/2
            }


        })
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