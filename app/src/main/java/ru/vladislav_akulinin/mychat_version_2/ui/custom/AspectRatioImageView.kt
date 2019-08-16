package ru.vladislav_akulinin.mychat_version_2.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import ru.vladislav_akulinin.mychat_version_2.R

//является custom view
class AspectRatioImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
) : ImageView(context, attrs, defStyleAttr){
    companion object{
        //значение по умолчанию для соотношения сторон
        private const val DEFAULT_ASPECT_RATIO = 1.78f
    }

    private var aspectRatio = DEFAULT_ASPECT_RATIO

    init {
        //если переданные атрибуты не равны null
        if(attrs!=null){
            //тогда обращаемся к контексту и считываем атрибуты
            val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
            aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO)
            a.recycle() //высвободить ресурс и необращаться к нему
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //новая высота, относительно текущего соотношения сторон
        val newHeight = (measuredWidth/aspectRatio).toInt()
        setMeasuredDimension(measuredWidth, newHeight)
    }
}