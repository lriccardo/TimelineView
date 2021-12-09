package com.lriccardo.timelineview

import androidx.annotation.ColorInt

interface TimelineAdapter {
    fun getTimelineViewType(position: Int): TimelineView.ViewType? = null
    fun getIndicatorColor(position: Int): Int? = null
    fun getLineColor(position: Int): Int? = null
}