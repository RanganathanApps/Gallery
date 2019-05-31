package apps.ranganathan.gallery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.utils.ScaleListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import android.view.MotionEvent
import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import apps.ranganathan.configlibrary.base.BaseAppActivity
import kotlin.math.absoluteValue


class ViewPagerAdapter(
    private val context: Context,
    list: List<Album>,
    position1: MutableLiveData<Int>
) : PagerAdapter() {
    private var layoutInflater : LayoutInflater? = null
    private var imgaeslist = list
    private var position1 = position1

    var d = 0f
    var newRot = 0f
    private var isZoomAndRotate: Boolean = false
    private var isOutSide: Boolean = false
    private var NONE = 0
    private var DRAG = 1
    private var ZOOM = 2
    var lastEvent: FloatArray? = null
    private var mode = NONE
    private var start = PointF()
    private var mid = PointF()
    var oldDist = 1f
    private var xCoOrdinate: Float = 0.toFloat()
    var yCoOrdinate: Float = 0.toFloat()


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ===  `object`
    }

    override fun getCount(): Int {
        return imgaeslist.size
    }

    private lateinit var mScaleGestureDetector: ScaleGestureDetector

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.item_picture_view , null)
        val imgAlbum = v.findViewById<View>(R.id.imgAlbum) as ImageView
        position1.value = position

        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener(imgAlbum))
                val activity :BaseAppActivity = context as BaseAppActivity
        activity.loadImage(imgaeslist[position].albumUri,
            imgAlbum,
            R.drawable.ic_camera_alt_white_24dp)
        val vp = container as ViewPager
        vp.addView(v , 0)


        return v

    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }


    private fun viewTransformation(view: View, event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xCoOrdinate = view.x - event.rawX
                yCoOrdinate = view.y - event.rawY

                start.set(event.x, event.y)
                isOutSide = false
                mode = DRAG
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    midPoint(mid, event)
                    mode = ZOOM
                }

                lastEvent = FloatArray(4)
                lastEvent!![0] = event.getX(0)
                lastEvent!![1] = event.getX(1)
                lastEvent!![2] = event.getY(0)
                lastEvent!![3] = event.getY(1)
                d = rotation(event)
            }
            MotionEvent.ACTION_UP -> {
                isZoomAndRotate = false
                if (mode === DRAG) {
                    val x = event.x
                    val y = event.y
                }
                isOutSide = true
                mode = NONE
                lastEvent = null
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_OUTSIDE -> {
                isOutSide = true
                mode = NONE
                lastEvent = null
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_MOVE -> if (!isOutSide) {
                if (mode === DRAG) {
                    isZoomAndRotate = false
                    view.animate().x(event.rawX + xCoOrdinate).y(event.rawY + yCoOrdinate).setDuration(0).start()
                }
                if (mode === ZOOM && event.pointerCount == 2) {
                    val newDist1 = spacing(event)
                    if (newDist1 > 10f) {
                        val scale = newDist1 / oldDist * view.scaleX
                        view.scaleX = scale
                        view.scaleY = scale
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event)
                        view.rotation = (view.rotation + (newRot - d)) as Float
                    }
                }
            }
        }
    }

    private fun rotation(event: MotionEvent): Float {
        val delta_x = (event.getX(0) - event.getX(1)).toDouble()
        val delta_y = (event.getY(0) - event.getY(1)).toDouble()
        val radians = Math.atan2(delta_y, delta_x)
        return Math.toDegrees(radians).toFloat()
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toInt().toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }
}