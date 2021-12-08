package com.lriccardo.timelineview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class TimelineDecorator(
    val indicatorRadius: Float = 24f,
    val lineWidth: Float = indicatorRadius / 1.61f,
    val padding: Float = indicatorRadius * 2,
    val position: Position = Position.Left,
    @ColorInt val indicatorColor: Int? = null,
    @ColorInt val lineColor: Int? = null
) : RecyclerView.ItemDecoration() {

    val width = ((indicatorRadius * 2) + (padding * 2))

    enum class Position {
        Left,
        Right
    }

    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        s: RecyclerView.State
    ) {
        when (position) {
            Position.Left ->
                rect.left = width.toInt()
            Position.Right ->
                rect.right = width.toInt()
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach {
            val itemPosition = parent.getChildAdapterPosition(it)

            if (itemPosition == RecyclerView.NO_POSITION)
                return

            val timelineView = TimelineView(context = parent.context)
            timelineView.setType(itemPosition, parent.adapter?.itemCount ?: -1)
            timelineView.indicatorRadius = indicatorRadius
            timelineView.lineWidth = lineWidth
            indicatorColor?.let {
                timelineView.indicatorColor = it
            }
            lineColor?.let {
                timelineView.lineColor = it
            }

            timelineView.measure(
                View.MeasureSpec.getSize(width.toInt()),
                View.MeasureSpec.getSize(it.measuredHeight)
            )
            c.save()
            when (position) {
                Position.Left -> {
                    c.translate(padding, it.top.toFloat())
                    timelineView.layout(0, 0, timelineView.measuredWidth, it.measuredHeight)
                }
                Position.Right -> {
                    c.translate(it.measuredWidth + padding, it.top.toFloat())
                    timelineView.layout(0, 0, timelineView.measuredWidth, it.measuredHeight)
                }
            }
            timelineView.draw(c)
            c.restore()
        }
    }
}