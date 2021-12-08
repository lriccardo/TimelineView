package com.lriccardo.timelineview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt


class TimelineView @JvmOverloads constructor(
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
    var lineWidth: Float

    @ColorInt
    var indicatorColor: Int

    @ColorInt
    var lineColor: Int

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TimelineView,
            0, 0
        ).apply {
            try {
                viewType = ViewType.values()[getInteger(R.styleable.TimelineView_timeline_item_type, 0)]
                indicatorRadius = getDimensionPixelSize(
                    R.styleable.TimelineView_indicator_radius,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, context.getResources().getDisplayMetrics())
                        .toInt()
                ).toFloat()
                lineWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_line_width,
                    (indicatorRadius / 1.61).toFloat().toInt()
                ).toFloat()
                indicatorColor = getColor(R.styleable.TimelineView_indicator_color, Color.RED)
                lineColor = getColor(R.styleable.TimelineView_line_color, Color.RED)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSizeAndState((indicatorRadius * 2).toInt(), widthMeasureSpec, 0)
        val height = resolveSizeAndState((indicatorRadius * 2).toInt(), heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var rectLeft = (width / 2) - (lineWidth / 2)
        var rectRight = (width / 2) + (lineWidth / 2)
        var rectTop = (height / 2).toFloat()
        var rectBottom = height.toFloat()

        var indicatorCenterX = (width / 2).toFloat()
        var indicatorCenterY = (height / 2).toFloat()

        var drawIndicator = true

        when (viewType) {
            ViewType.FIRST -> {
                rectTop = (height / 2).toFloat()
                rectBottom = height.toFloat()
            }
            ViewType.MIDDLE -> {
                rectTop = 0f
                rectBottom = height.toFloat()

            }
            ViewType.LAST -> {
                rectTop = 0f
                rectBottom = (height / 2).toFloat()

            }
            ViewType.SPACER -> {
                rectTop = 0f
                rectBottom = height.toFloat()
                drawIndicator = false
            }
        }

        paint.color = lineColor
        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint)

        if (drawIndicator) {
            paint.color = indicatorColor
            canvas.drawCircle(indicatorCenterX, indicatorCenterY, indicatorRadius, paint)
        }
    }

    fun setType(position: Int, totalItems: Int) {
        when (position) {
            0 -> viewType = ViewType.FIRST
            totalItems - 1 -> viewType = ViewType.LAST
            else -> viewType = ViewType.MIDDLE
        }
    }

}