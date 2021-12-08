package com.lriccardo.timelineview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class TimelineDecorator(
    val indicatorRadius: Float = 24f,
    val lineWidth: Float = indicatorRadius/1.61f,
    val padding: Float = indicatorRadius*2,
    val position: Position = Position.Left
) : RecyclerView.ItemDecoration() {

    val width = ((indicatorRadius * 2)+(padding*2))

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
        when(position){
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
            timelineView.indicatorRadius = indicatorRadius.toFloat()
            timelineView.lineWidth = lineWidth.toFloat()

            timelineView.measure(
                View.MeasureSpec.getSize(width.toInt()),
                View.MeasureSpec.getSize(it.measuredHeight)
            )
            c.save()
            when(position){
                Position.Left -> {
                    c.translate(padding.toFloat(), it.top.toFloat())
                    timelineView.layout(0, 0, timelineView.measuredWidth, it.measuredHeight)
                }
                Position.Right -> {
                    c.translate(it.measuredWidth+padding.toFloat(), it.top.toFloat())
                    timelineView.layout(0, 0, timelineView.measuredWidth, it.measuredHeight)
                }
            }
            timelineView.draw(c)
            c.restore()
        }
    }
}