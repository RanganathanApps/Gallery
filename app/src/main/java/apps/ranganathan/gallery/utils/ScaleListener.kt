package apps.ranganathan.gallery.utils

import android.view.ScaleGestureDetector
import android.widget.ImageView


open class ScaleListener(imageView: ImageView) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var mScaleFactor: Float = 0.0f
    private var mImageView: ImageView = imageView

    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        mScaleFactor *= scaleGestureDetector.scaleFactor
        mScaleFactor = Math.max(
            0.1f,
            Math.min(mScaleFactor, 10.0f)
        )
        mImageView.setScaleX(mScaleFactor)
        mImageView.setScaleY(mScaleFactor)
        return true
    }
}