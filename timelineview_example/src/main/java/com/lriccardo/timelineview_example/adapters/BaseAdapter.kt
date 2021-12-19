package com.lriccardo.timelineview_example.adapters

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lriccardo.timelineview.TimelineAdapter
import com.lriccardo.timelineview.TimelineView
import com.lriccardo.timelineview_example.R
import com.lriccardo.timelineview_example.viewholders.BaseViewHolder

class BaseAdapter(val items: List<Int>) : RecyclerView.Adapter<BaseViewHolder>(), TimelineAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.base_list_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.title.text = position.toString()
    }

    override fun getItemViewType(position: Int): Int {
        return 0;
    }

    override fun getItemCount(): Int = items.size

    override fun getIndicatorStyle(position: Int): TimelineView.IndicatorStyle? {
        if (position <= 1)
            return TimelineView.IndicatorStyle.Checked
        else return TimelineView.IndicatorStyle.Empty
    }

    override fun getLineStyle(position: Int): TimelineView.LineStyle? {
        if (position > 1)
            return TimelineView.LineStyle.Dashed
        return super.getLineStyle(position)
    }

    override fun getLinePadding(position: Int): Float? {
        if (position > 1)
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                Resources.getSystem().displayMetrics
            )
        return super.getLinePadding(position)
    }
}