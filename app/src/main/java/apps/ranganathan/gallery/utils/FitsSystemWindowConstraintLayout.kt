package apps.ranganathan.gallery.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class FitsSystemWindowConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var childsMargins: HashMap<Any, IntArray>
    var statusBarBackgroundDrawable: Drawable? = null
        private set
    private var mDrawStatusBarBackground: Boolean = false

    private var mLastInsets: WindowInsetsCompat? = null


    init {

        if (ViewCompat.getFitsSystemWindows(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                ViewCompat.setOnApplyWindowInsetsListener(
                    this,
                    object : OnApplyWindowInsetsListener,
                        androidx.core.view.OnApplyWindowInsetsListener {
                        override fun onApplyWindowInsets(v: View?, insets: WindowInsets?): WindowInsets {
                            return insets!!.consumeSystemWindowInsets()
                        }

                        override fun onApplyWindowInsets(view: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                            val layout = view as FitsSystemWindowConstraintLayout
                            layout.setChildInsets(insets, insets.systemWindowInsetTop > 0)
                            return insets.consumeSystemWindowInsets()
                        }
                    })
            }
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.colorPrimaryDark))
            try {
                statusBarBackgroundDrawable = typedArray.getDrawable(0)
            } finally {
                typedArray.recycle()
            }
        } else {
            statusBarBackgroundDrawable = null
        }
    }

    fun setChildInsets(insets: WindowInsetsCompat, draw: Boolean) {
        mLastInsets = insets
        mDrawStatusBarBackground = draw
        setWillNotDraw(!draw && background == null)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility !== View.GONE) {
                if (ViewCompat.getFitsSystemWindows(this)) {
                    val layoutParams = child.layoutParams as ConstraintLayout.LayoutParams

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        ViewCompat.dispatchApplyWindowInsets(child, insets)
                    } else {
                        var childMargins: IntArray? = childsMargins.get(child)
                        if (childMargins == null) {
                            childMargins = intArrayOf(
                                layoutParams.leftMargin,
                                layoutParams.topMargin,
                                layoutParams.rightMargin,
                                layoutParams.bottomMargin
                            )
                            childsMargins.put(child, childMargins)
                        }
                        if (layoutParams.leftToLeft == ConstraintLayout.LayoutParams.PARENT_ID) {
                            layoutParams.leftMargin = childMargins[0] + insets.systemWindowInsetLeft
                        }
                        if (layoutParams.topToTop == ConstraintLayout.LayoutParams.PARENT_ID) {
                            layoutParams.topMargin = childMargins[1] + insets.systemWindowInsetTop
                        }
                        if (layoutParams.rightToRight == ConstraintLayout.LayoutParams.PARENT_ID) {
                            layoutParams.rightMargin = childMargins[2] + insets.systemWindowInsetRight
                        }
                        if (layoutParams.bottomToBottom == ConstraintLayout.LayoutParams.PARENT_ID) {
                            layoutParams.bottomMargin = childMargins[3] + insets.systemWindowInsetBottom
                        }
                    }
                }
            }
        }

        requestLayout()
    }

    fun setStatusBarBackground(bg: Drawable) {
        statusBarBackgroundDrawable = bg
        invalidate()
    }

    fun setStatusBarBackground(resId: Int) {
        statusBarBackgroundDrawable = if (resId != 0) ContextCompat.getDrawable(context, resId) else null
        invalidate()
    }

    fun setStatusBarBackgroundColor(@ColorInt color: Int) {
        statusBarBackgroundDrawable = ColorDrawable(color)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDrawStatusBarBackground && statusBarBackgroundDrawable != null) {
            val inset = if (mLastInsets != null) mLastInsets!!.systemWindowInsetTop else 0
            if (inset > 0) {
                statusBarBackgroundDrawable!!.setBounds(0, 0, width, inset)
                statusBarBackgroundDrawable!!.draw(canvas)
            }
        }
    }
}