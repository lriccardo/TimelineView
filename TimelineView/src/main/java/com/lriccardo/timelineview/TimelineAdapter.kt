package com.lriccardo.timelineview

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

interface TimelineAdapter {
    fun getTimelineViewType(position: Int): TimelineView.ViewType? = null
    fun getIndicatorDrawable(position: Int): Drawable? = null
    @DrawableRes fun getIndicatorDrawableRes(position: Int): Int? = null
    fun getIndicatorStyle(position: Int): TimelineView.IndicatorStyle? = null
    fun getIndicatorColor(position: Int): Int? = null
    fun getLineColor(position: Int): Int? = null
    fun getLineStyle(position: Int): TimelineView.LineStyle? = null
    fun getLinePadding(position: Int): Float? = null
}