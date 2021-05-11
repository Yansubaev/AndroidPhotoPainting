package com.yans.painting.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.drawToBitmap
import com.yans.painting.R

class PaintView : View {

    private lateinit var paint: Paint

    private lateinit var brushPaint: Paint

    private var drawableBitmap: Bitmap? = null

    private var destRect: Rect? = null

    private var brushPaths: MutableList<Pair<Path, Paint>> = mutableListOf()

    //region Constructors
    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }
    //endregion

    private fun init(attrs: AttributeSet?) {
        paint = Paint()
        paint.color = Color.RED
        brushPaint = Paint()
        brushPaint.flags = Paint.ANTI_ALIAS_FLAG
        brushPaint.style = Paint.Style.STROKE
        brushPaint.pathEffect = CornerPathEffect(50f)
        attrs?.let { attrs ->
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PaintView)
            brushPaint.strokeWidth = ta.getDimension(R.styleable.PaintView_brush_width, 5f)
            brushPaint.color = ta.getColor(R.styleable.PaintView_brush_default_color, Color.WHITE)
            ta.recycle()
        }
    }

    fun getPaintedImage() : Bitmap = drawToBitmap(Bitmap.Config.ARGB_8888)


    fun setImageBitmap(bitmap: Bitmap) {
        drawableBitmap = bitmap
        destRect = Rect(0, 0, width, height)
    }

    fun setBrushColor(color: Int) {
        brushPaint.color = color
    }

    fun setBrushThickness(thickness: Int){
        brushPaint.strokeWidth = thickness.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        drawableBitmap?.let { bitmap ->
            destRect?.right = width
            destRect?.bottom = height
            destRect?.let { destRect ->
                canvas?.drawBitmap(bitmap, null, destRect, paint)
            }
        }

        brushPaths.forEach { pair ->
            canvas?.drawPath(pair.first, pair.second)
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                brushPaths.add(Pair(Path(), Paint(brushPaint)) )
                brushPaths.last().first.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                brushPaths.last().first.lineTo(event.x, event.y)
            }
        }
        invalidate()
        return true
    }
}