package com.lriccardo.timelineview_example.activities

import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lriccardo.timelineview.TimelineDecorator
import com.lriccardo.timelineview_example.R
import com.lriccardo.timelineview_example.adapters.BaseAdapter
import com.lriccardo.timelineview_example.databinding.ActivityRecyclerViewDecoratorBinding

class RecyclerViewDecoratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerViewDecoratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewDecoratorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.timelineRv.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = BaseAdapter((0..10).toList())

            val colorPrimary = TypedValue()
            val theme: Theme = getTheme()
            theme.resolveAttribute(R.attr.colorPrimary, colorPrimary, true)

            it.addItemDecoration(
                TimelineDecorator(
                    position = TimelineDecorator.Position.Left,
                    indicatorColor = colorPrimary.data,
                    lineColor = colorPrimary.data
                )
            )

            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    (it.layoutManager as? LinearLayoutManager)?.let {
                        if (it.findFirstCompletelyVisibleItemPosition() == 0)
                            binding.fab.extend()
                        else
                            binding.fab.shrink()
                    }
                }
            })
        }
    }
}