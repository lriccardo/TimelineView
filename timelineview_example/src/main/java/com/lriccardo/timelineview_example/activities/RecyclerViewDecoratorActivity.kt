package com.lriccardo.timelineview_example.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lriccardo.timelineview.TimelineDecorator
import com.lriccardo.timelineview_example.adapters.BaseAdapter
import com.lriccardo.timelineview_example.databinding.ActivityRecyclerViewBinding

class RecyclerViewDecoratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.timelineRv.let {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = BaseAdapter((0..10).toList())
            it.addItemDecoration(TimelineDecorator())
        }
    }
}