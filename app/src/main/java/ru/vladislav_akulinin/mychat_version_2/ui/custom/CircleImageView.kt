package ru.vladislav_akulinin.mychat_version_2.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import ru.vladislav_akulinin.mychat_version_2.App
import ru.vladislav_akulinin.mychat_version_2.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {
    private var borderColor = Color.WHITE
    private var borderWidth = (2 * resources.displayMetrics.density + 0.5f).toInt()

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, borderColor)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, borderWidth)
            a.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setImageDrawable(BitmapDrawable(resources, transformToCircleWithColorBorder(getBitmapFromDrawable(), borderColor, borderWidth)))
    }

    private fun getBitmapFromDrawable(): Bitmap {
        if (drawable is BitmapDrawable) return (drawable as BitmapDrawable).bitmap

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun transformToCircleWithColorBorder(bitmap: Bitmap, borderColor: Int, borderWidth: Int): Bitmap {
        val minSize = min(bitmap.width, bitmap.height)
        val result = Bitmap.createBitmap(minSize, minSize, Bitmap.Config.ARGB_8888)
        var paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }

        val canvas = Canvas(result)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(minSize / 2F, minSize / 2F, minSize / 2F, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        val rect = Rect(0, 0, minSize, minSize)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        paint = Paint().apply {
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth.toFloat()
        }
        canvas.drawCircle(minSize / 2F, minSize / 2F, minSize / 2F, paint)

        return result
    }

    @Dimension fun getBorderWidth(): Int = (borderWidth / resources.displayMetrics.density + 0.5f).toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = (borderWidth * resources.displayMetrics.density + 0.5f).toInt()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(App.applicationContext(), colorId)
    }

    fun updateAvatar(initials: String?) {
        if (initials != null) {
            val image = Bitmap.createBitmap(layoutParams.height, layoutParams.height, Bitmap.Config.ARGB_8888)
            val color = TypedValue()

            val canvas = Canvas(image)
            canvas.drawColor(color.data)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = 48f * context.resources.displayMetrics.scaledDensity.toInt()
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER

            val textBounds = Rect()
            paint.getTextBounds(initials, 0, initials.length, textBounds)

            val backgroundBounds = RectF()
            backgroundBounds.set(0f, 0f, layoutParams.height.toFloat(), layoutParams.height.toFloat())

            val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()
            val canvas1 = Canvas(image)
            canvas1.drawText(initials, backgroundBounds.centerX(), textBottom, paint)

            setImageDrawable(BitmapDrawable(resources, transformToCircleWithColorBorder(image, borderColor, borderWidth)))
        } else {
            setImageDrawable(BitmapDrawable(resources, transformToCircleWithColorBorder(getBitmapFromDrawable(), borderColor, borderWidth)))
        }
        invalidate()
    }
}