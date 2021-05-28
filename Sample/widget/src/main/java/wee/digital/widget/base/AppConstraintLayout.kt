package wee.digital.widget.base

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout
import wee.digital.widget.R

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy 2020/10/12
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class AppConstraintLayout : ConstraintLayout {

    var path: Path? = null

    /** corner radius */
    var cornerLeftTop: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerRightTop: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerLeftBottom: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerRightBottom: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var backgroundColor: Int? = null
        set(@ColorInt value) {
            field = value
            postInvalidate()
        }

    var strokeLineWidth: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var strokeLineColor = 0XFFFFFFFF.toInt()
        set(@ColorInt value) {
            field = value
            postInvalidate()
        }

    var dashLineGap: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var dashLineWidth: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        render(attrs)
    }

    private fun render(attrs: AttributeSet?) {
        attrs?.let {
            /** set corner radii */
            context.obtainStyledAttributes(it, wee.digital.library.R.styleable.AppConstraintLayout).apply {
                val radius = pixels(wee.digital.library.R.styleable.AppConstraintLayout_radius)

                if (radius != 0f) {
                    cornerLeftTop = radius
                    cornerRightTop = radius
                    cornerLeftBottom = radius
                    cornerRightBottom = radius
                } else {
                    cornerLeftTop = pixels(wee.digital.library.R.styleable.AppConstraintLayout_topLeftRadius)
                    cornerRightTop = pixels(wee.digital.library.R.styleable.AppConstraintLayout_topRightRadius)
                    cornerLeftBottom = pixels(wee.digital.library.R.styleable.AppConstraintLayout_bottomLeftRadius)
                    cornerRightBottom = pixels(wee.digital.library.R.styleable.AppConstraintLayout_bottomRightRadius)
                }

                backgroundColor = getColor(wee.digital.library.R.styleable.AppConstraintLayout_backgroundColor, Color.WHITE)
                strokeLineWidth = pixels(wee.digital.library.R.styleable.AppConstraintLayout_strokeLineWidth)
                strokeLineColor = getColor(wee.digital.library.R.styleable.AppConstraintLayout_strokeLineColor, Color.BLACK)
                dashLineWidth = pixels(wee.digital.library.R.styleable.AppConstraintLayout_dashLineWidth)
                dashLineGap = pixels(wee.digital.library.R.styleable.AppConstraintLayout_dashLineGap)

            }.run {
                recycle()
            }
        }
    }

    fun TypedArray.pixels(@StyleableRes id: Int): Float {
        return getDimensionPixelSize(id, 0).toFloat()
    }

    override fun dispatchDraw(canvas: Canvas) {
        /** for outline remake when ever draw */
        path = Path()

        clipPathCanvas(canvas, floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop, cornerRightBottom,
                cornerRightBottom, cornerLeftBottom, cornerLeftBottom
        ))

        /** set drawable resource corner & background & stroke */
        GradientDrawable().apply {
            cornerRadii = floatArrayOf(
                    cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop,
                    cornerRightBottom, cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )
            if (strokeLineWidth != 0f && strokeLineColor != null) {
                this.setStroke(strokeLineWidth.toInt(), strokeLineColor, dashLineWidth, dashLineGap)
            }
            backgroundColor?.let {
                setColor(it)
            } ?: setColor(Color.WHITE)

            background = this
        }

        outlineProvider = outlineProvider

        clipChildren = false

        super.dispatchDraw(canvas)
    }

    private fun clipPathCanvas(canvas: Canvas, floatArray: FloatArray) {
        path?.let {
            it.addRoundRect(
                    RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()),
                    floatArray,
                    Path.Direction.CW
            )
            canvas.clipPath(it)
        }
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
    }

    /** For not showing red underline */
    override fun setOutlineProvider(provider: ViewOutlineProvider?) {
        super.setOutlineProvider(provider)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                path?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        outline.setPath(it)
                    } else {
                        @Suppress("DEPRECATION")
                        outline.setConvexPath(it)
                    }
                } ?: throw Exception()
            }
        }
    }

}