package com.lriccardo.timelineview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
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

    var indicatorDrawable: Drawable?

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

    var indicatorYPosition: Float = 0.5f
        set(value) {
            field = value.coerceIn(0f, 1f)
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
    var linePadding: Float = 0.toPx().toFloat()
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
                    ViewType.values()[getInteger(
                        R.styleable.TimelineView_timeline_item_type,
                        viewType.ordinal
                    )]

                indicatorDrawable = getDrawable(R.styleable.TimelineView_indicator_drawable)

                indicatorSize = getDimensionPixelSize(
                    R.styleable.TimelineView_indicator_size,
                    indicatorSize.toInt()
                ).toFloat()

                checkedIndicatorSize = getDimensionPixelSize(
                    R.styleable.TimelineView_checked_indicator_size,
                    checkedIndicatorSize.toInt()
                ).toFloat()

                checkedIndicatorStrokeWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_checked_indicator_stroke_width,
                    checkedIndicatorStrokeWidth.toInt()
                ).toFloat()

                indicatorYPosition = getFloat(
                    R.styleable.TimelineView_indicator_y_position,
                    indicatorYPosition
                ).coerceIn(0f, 1f)

                lineWidth = getDimensionPixelSize(
                    R.styleable.TimelineView_line_width,
                    lineWidth.toInt()
                ).toFloat()

                linePadding = getDimensionPixelSize(
                    R.styleable.TimelineView_line_padding,
                    linePadding.toInt()
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

        val indicatorCenterX = (width / 2).toFloat()
        val indicatorCenterY =
            (height * indicatorYPosition).coerceIn(indicatorSize, height - indicatorSize)

        var lineX = (width / 2).toFloat()

        var topLineYStart = 0f
        var topLineYEnd: Float = indicatorCenterY

        val bottomLineYStart = height.toFloat()
        var bottomLineYEnd = indicatorCenterY

        // The top line should start with a gap, so we start drawing below 'lineDashGap'
        if (lineStyle == LineStyle.Dashed && (indicatorCenterY - indicatorSize) > lineDashGap)
            topLineYStart += lineDashGap

        val drawIndicator = viewType != ViewType.SPACER
        val drawTopLine = viewType != ViewType.FIRST
        val drawBottomLine = viewType != ViewType.LAST

        if (drawIndicator) {
            if (indicatorDrawable != null) {
                drawDrawableIndicator(indicatorCenterX, indicatorCenterY, canvas)

                indicatorDrawable?.bounds?.let {
                    topLineYEnd =
                        (it.top - linePadding).coerceAtLeast(topLineYStart)
                    bottomLineYEnd =
                        (it.bottom + linePadding).coerceAtMost(bottomLineYStart)
                    lineX = it.centerX().toFloat()
                }
            } else {
                drawCircleIndicator(canvas, indicatorCenterX, indicatorCenterY)

                topLineYEnd =
                    (indicatorCenterY - indicatorSize - linePadding).coerceAtLeast(topLineYStart)
                bottomLineYEnd =
                    (indicatorCenterY + indicatorSize + linePadding).coerceAtMost(bottomLineYStart)
            }
        } else {
            topLineYEnd = indicatorCenterY
            bottomLineYEnd = indicatorCenterY
        }

        if (drawTopLine) {
            canvas.drawLine(lineX, topLineYStart, lineX, topLineYEnd, linePaint)
        }

        if (drawBottomLine) {
            canvas.drawLine(lineX, bottomLineYStart, lineX, bottomLineYEnd, linePaint)
        }
    }

    private fun drawCircleIndicator(
        canvas: Canvas,
        indicatorCenterX: Float,
        indicatorCenterY: Float
    ) {
        if (indicatorStyle == IndicatorStyle.Checked) {
            // Outer circle
            canvas.drawCircle(
                indicatorCenterX,
                indicatorCenterY,
                indicatorSize,
                indicatorPaint
            )

            // Inner circle
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

    private fun drawDrawableIndicator(
        indicatorCenterX: Float,
        indicatorCenterY: Float,
        canvas: Canvas
    ) {
        val left = (indicatorCenterX - indicatorSize).toInt()
        val top = (indicatorCenterY - indicatorSize).toInt()
        val right = (indicatorCenterX + indicatorSize).toInt()
        val bottom = (indicatorCenterY + indicatorSize).toInt()
        indicatorDrawable?.setBounds(left, top, right, bottom)
        indicatorDrawable?.draw(canvas)
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