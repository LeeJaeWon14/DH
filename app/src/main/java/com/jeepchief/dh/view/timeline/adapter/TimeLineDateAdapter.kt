package com.jeepchief.dh.view.timeline.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.TimeLineRows
import com.jeepchief.dh.util.Log

class TimeLineDateAdapter(private val map: HashMap<String, MutableList<TimeLineRows>>) : RecyclerView.Adapter<TimeLineDateAdapter.TimeLineDateViewHolder>() {
    private val keys get() = map.keys.sortedDescending()
    class TimeLineDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTimeLineDate: TextView = view.findViewById(R.id.tv_timeline_date)
        val rvTimeLineDate: RecyclerView = view.findViewById(R.id.rv_timeline_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineDateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline_date, parent, false)
        return TimeLineDateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeLineDateViewHolder, position: Int) {
        holder.apply {
            map.get(keys[position])?.let {
                tvTimeLineDate.text = keys[position]
                rvTimeLineDate.apply {
                    layoutManager = LinearLayoutManager(itemView.context)
                    adapter = TimeLineAdapter(it)
                }
            } ?: run {
                Log.e("not find..")
            }
        }
    }

    override fun getItemCount(): Int = map.size
}