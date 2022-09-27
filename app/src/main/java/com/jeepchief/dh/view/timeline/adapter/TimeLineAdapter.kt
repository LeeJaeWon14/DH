package com.jeepchief.dh.view.timeline.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.TimeLineRows
import com.jeepchief.dh.util.RarityChecker

class TimeLineAdapter(private val list: List<TimeLineRows>) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {
    class TimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvTimeline: TextView = view.findViewById(R.id.tv_timeline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_line, parent, false)
        return TimeLineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvDate.text = date
                if(name.contains("전직")) {
                    tvTimeline.text = data.jobGrowName.plus(" 전직")
                }
                else if(name.contains("획득")) {
                    tvTimeline.run {
                        text = data.itemName.plus(" 획득")
                        setTextColor(RarityChecker.convertColor(data.itemRarity!!))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}