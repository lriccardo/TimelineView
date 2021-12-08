package com.lriccardo.timelineview_example.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lriccardo.timelineview_example.R
import com.lriccardo.timelineview_example.viewholders.BaseViewHolder

class BaseAdapter(val items: List<Int>): RecyclerView.Adapter<BaseViewHolder>() {
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
}