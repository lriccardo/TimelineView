package com.lriccardo.timelineview

interface TimelineAdapter {
    fun getTimelineViewType(position: Int): TimelineView.ViewType? = null
    fun getIndicatorStyle(position: Int): TimelineView.IndicatorStyle? = null
    fun getIndicatorColor(position: Int): Int? = null
    fun getLineColor(position: Int): Int? = null
    fun getLineStyle(position: Int): TimelineView.LineStyle? = null
    fun getLinePadding(position: Int): Float? = null
}