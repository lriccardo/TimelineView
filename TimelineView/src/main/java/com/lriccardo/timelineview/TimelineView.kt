package com.lriccardo.timelineview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
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

    enum class LineStyle {
        Normal,
        Dashed
    }

    var viewType = ViewType.FIRST

    var indicatorSize: Float = 12.toPx().toFloat()
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

    var checkedIndicatorSize: Float = 6.toPx().toFloat()
    var checkedIndicatorStrokeWidth: Float = 4.toPx().toFloat()
        set(value) {
            field = value
            initIndicatorPaint()
        }

    var lineStyle = LineStyle.Normal
        set(value) {
            field = value
            initLinePaint()
        }
    var lineWidth: Float = 8.toPx().toFloat()
        set(value) {
            field = value
            initLinePaint()
        }
    var lineDashLength: Float = 18.toPx().toFloat()
        set(value) {
            field = value
            initLinePaint()
        }
    var lineDashGap: Float = 12.toPx().toFloat()
        set(value) {
            field = value
            initLinePaint()
        }

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
        style = Paint.Style.STROKE
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TimelineView,
            0, 0
        ).apply {
            try {
                viewType =
                    ViewType.values()[getInteger(R.styleable.TimelineView_timeline_item_type, viewType.ordinal)]

                indicatorSize = getDimensionPixelSize(
                    R.styleable.TimelineView_indicator_size,
                    indicatorSize.toInt()
                ).toFloat()

                checkedIndicatorSize = getDimensionPixelSize(
                    R.styleable.TimelineView_checked_indicator_size,
                    checkedIndicatorSize.toInt()
                ).toFloat()

                lineWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_line_width,
                    lineWidth.toInt()
                ).toFloat()

                lineDashLength = getDimensionPixelSize(
                    R.styleable.TimelineView_line_dash_length,
                    lineDashLength.toInt()
                ).toFloat()

                lineDashGap = getDimensionPixelSize(
                    R.styleable.TimelineView_line_dash_gap,
                    lineDashGap.toInt()
                ).toFloat()

                lineStyle =
                    LineStyle.values()[getInteger(R.styleable.TimelineView_line_style, lineStyle.ordinal)]

                lineColor = getColor(R.styleable.TimelineView_line_color, lineColor)

                checkedIndicatorStrokeWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_checked_indicator_stroke_width,
                    checkedIndicatorStrokeWidth.toInt()
                ).toFloat()

                indicatorColor = getColor(R.styleable.TimelineView_indicator_color, indicatorColor)
                indicatorStyle =
                    IndicatorStyle.values()[getInteger(R.styleable.TimelineView_indicator_style, indicatorStyle.ordinal)]

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
                    style = Paint.Style.FILL_AND_STROKE
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
            when(lineStyle){
                LineStyle.Normal -> {
                    pathEffect = PathEffect()
                }
                LineStyle.Dashed -> {
                    pathEffect = DashPathEffect(floatArrayOf(lineDashLength, lineDashGap), 0.0f)
                }
            }
            strokeWidth = lineWidth
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = when(indicatorStyle){
            IndicatorStyle.Filled -> indicatorSize*2
            IndicatorStyle.Empty -> (indicatorSize*2) + checkedIndicatorStrokeWidth
            IndicatorStyle.Checked -> (indicatorSize*2) + checkedIndicatorStrokeWidth
        }.toInt()

        val width = resolveSizeAndState(size, widthMeasureSpec, 0)
        val height = resolveSizeAndState(size, heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val lineX = (width / 2).toFloat()

        var topLineYStart: Float
        var topLineYEnd: Float

        var bottomLineYStart: Float
        var bottomLineYEnd: Float

        val indicatorCenterX = (width / 2).toFloat()
        val indicatorCenterY = (height / 2).toFloat()

        var drawIndicator = true
        var drawTopLine = false
        var drawBottomLine = false

        when (viewType) {
            ViewType.FIRST -> {
                drawTopLine = false
                drawBottomLine = true
            }
            ViewType.MIDDLE -> {
                drawTopLine = true
                drawBottomLine = true
            }
            ViewType.LAST -> {
                drawTopLine = true
                drawBottomLine = false
            }
            ViewType.SPACER -> {
                drawTopLine = true
                drawBottomLine = true
            }
        }
        topLineYStart = 0f
        if(lineStyle == LineStyle.Dashed)
            topLineYStart += lineDashGap

        topLineYEnd = (indicatorCenterY - indicatorSize) + 1f

        bottomLineYStart = height.toFloat()
        bottomLineYEnd = (indicatorCenterY + indicatorSize)- 1f

        if(drawTopLine) {
            canvas.drawLine(lineX, topLineYStart, lineX, topLineYEnd, linePaint)
        }
        if(drawBottomLine){
            canvas.drawLine(lineX, bottomLineYStart, lineX, bottomLineYEnd, linePaint)
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