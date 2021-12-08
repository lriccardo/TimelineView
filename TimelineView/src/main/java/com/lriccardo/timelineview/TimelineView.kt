package com.lriccardo.timelineview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class TimelineView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class ViewType {
        FIRST,
        MIDDLE,
        LAST,
        SPACER
    }

    var viewType = ViewType.FIRST
    var indicatorRadius: Float

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TimelineView,
            0, 0).apply {
            try {
                viewType = ViewType.values()[getInteger(R.styleable.TimelineView_tv_view_type, 0)]
                indicatorRadius = getDimensionPixelSize(R.styleable.TimelineView_tv_indicator_radius, 30).toFloat()
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSizeAndState((indicatorRadius*2).toInt(), widthMeasureSpec, 0)
        val height = resolveSizeAndState((indicatorRadius*2).toInt(), heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.RED
        var rectLeft = (width / 2)-(indicatorRadius/3)
        var rectRight = (width / 2)+(indicatorRadius/3)
        var rectTop = (height/2).toFloat()
        var rectBottom = height.toFloat()

        var indicatorCenterX = (width/2).toFloat()
        var indicatorCenterY = (height/2).toFloat()

        var drawIndicator = true

        when(viewType){
            ViewType.FIRST -> {
                rectLeft = (width / 2)-(indicatorRadius/3)
                rectRight = (width / 2)+(indicatorRadius/3)
                rectTop = (height/2).toFloat()
                rectBottom = height.toFloat()
            }
            ViewType.MIDDLE-> {
                rectLeft = (width / 2)-(indicatorRadius/3)
                rectRight = (width / 2)+(indicatorRadius/3)
                rectTop = 0f
                rectBottom = height.toFloat()

            }
            ViewType.LAST-> {
                rectLeft = (width / 2)-(indicatorRadius/3)
                rectRight = (width / 2)+(indicatorRadius/3)
                rectTop = 0f
                rectBottom = (height/2).toFloat()

            }
            ViewType.SPACER-> {
                rectLeft = (width / 2)-(indicatorRadius/3)
                rectRight = (width / 2)+(indicatorRadius/3)
                rectTop = 0f
                rectBottom = height.toFloat()
                drawIndicator = false
            }
        }

        if(drawIndicator)
            canvas.drawCircle(indicatorCenterX, indicatorCenterY, indicatorRadius, paint)

        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint)
    }
}