package com.lriccardo.timelineview

import android.content.Context
import android.content.res.Resources
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

    enum class IndicatorStyle {
        Filled,
        Empty,
        Checked
    }

    var viewType = ViewType.FIRST

    var indicatorSize: Float
    @ColorInt
    var indicatorColor: Int = Color.RED
        set(value) {
            field = value
            initIndicatorPaint()
        }
    var indicatorStyle = IndicatorStyle.Filled
        set(value) {
            field = value
            initIndicatorPaint()
        }

    var checkedIndicatorSize: Float
    var checkedIndicatorStrokeWidth: Float = 4.toPx().toFloat()
        set(value) {
            field = value
            initIndicatorPaint()
        }


    var lineWidth: Float
    @ColorInt
    var lineColor: Int = Color.RED
        set(value) {
            field = value
            initLinePaint()
        }


    private var indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var checkedIndicatorPaint: Paint? = null

    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TimelineView,
            0, 0
        ).apply {
            try {
                viewType =
                    ViewType.values()[getInteger(R.styleable.TimelineView_timeline_item_type, 0)]

                indicatorSize = getDimensionPixelSize(
                    R.styleable.TimelineView_indicator_size,
                    12.toPx()
                ).toFloat()

                checkedIndicatorSize = getDimensionPixelSize(
                    R.styleable.TimelineView_checked_indicator_size,
                    6.toPx()
                ).toFloat()

                lineWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_line_width,
                    8.toPx()
                ).toFloat()

                checkedIndicatorStrokeWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_checked_indicator_stroke_width,
                    4.toPx()
                ).toFloat()

                indicatorColor = getColor(R.styleable.TimelineView_indicator_color, Color.RED)
                lineColor = getColor(R.styleable.TimelineView_line_color, Color.RED)
                indicatorStyle =
                    IndicatorStyle.values()[getInteger(R.styleable.TimelineView_indicator_style, 2)]

                initIndicatorPaint()
                initLinePaint()
            } finally {
                recycle()
            }
        }
    }

    private fun initIndicatorPaint() {
        indicatorPaint.apply {
            when (indicatorStyle) {
                IndicatorStyle.Filled -> {
                    style = Paint.Style.FILL
                    color = indicatorColor
                }
                IndicatorStyle.Empty -> {
                    style = Paint.Style.STROKE
                    strokeWidth = checkedIndicatorStrokeWidth
                    color = indicatorColor
                }
                IndicatorStyle.Checked -> {
                    style = Paint.Style.STROKE
                    strokeWidth = checkedIndicatorStrokeWidth
                    color = indicatorColor
                    checkedIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        style = Paint.Style.FILL
                        color = indicatorColor
                    }
                }
            }
        }
    }

    private fun initLinePaint() {
        linePaint.apply {
            color = lineColor
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSizeAndState((indicatorSize * 2).toInt(), widthMeasureSpec, 0)
        val height = resolveSizeAndState((indicatorSize * 2).toInt(), heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var rectLeft = (width / 2) - (lineWidth / 2)
        var rectRight = (width / 2) + (lineWidth / 2)
        var rectTop = (height / 2).toFloat() + indicatorSize
        var rectBottom = height.toFloat()

        var indicatorCenterX = (width / 2).toFloat()
        var indicatorCenterY = (height / 2).toFloat()

        var drawIndicator = true

        when (viewType) {
            ViewType.FIRST -> {
                rectTop = indicatorCenterY + indicatorSize
                rectBottom = height.toFloat()
            }
            ViewType.MIDDLE -> {
                rectTop = 0f
                rectBottom = height.toFloat()
            }
            ViewType.LAST -> {
                rectTop = 0f
                rectBottom = indicatorCenterY - indicatorSize
            }
            ViewType.SPACER -> {
                rectTop = 0f
                rectBottom = height.toFloat()
                drawIndicator = false
            }
        }

        if (viewType == ViewType.MIDDLE) {
            canvas.drawRect(
                rectLeft,
                rectTop,
                rectRight,
                indicatorCenterY - indicatorSize,
                linePaint
            )
            canvas.drawRect(
                rectLeft,
                indicatorCenterY + indicatorSize,
                rectRight,
                rectBottom,
                linePaint
            )
        } else {
            canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, linePaint)
        }

        if (drawIndicator) {
            if (indicatorStyle == IndicatorStyle.Checked) {
                canvas.drawCircle(
                    indicatorCenterX,
                    indicatorCenterY,
                    indicatorSize,
                    indicatorPaint
                )
                checkedIndicatorPaint?.let {
                    it.color = indicatorColor
                    canvas.drawCircle(
                        indicatorCenterX,
                        indicatorCenterY,
                        checkedIndicatorSize,
                        it
                    )
                }
            } else {
                canvas.drawCircle(
                    indicatorCenterX,
                    indicatorCenterY,
                    indicatorSize,
                    indicatorPaint
                )
            }
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

internal fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()