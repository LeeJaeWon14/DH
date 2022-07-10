package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.Status
import com.jeepchief.dh.util.Log

class InfoRecyclerAdapter() : RecyclerView.Adapter<InfoRecyclerAdapter.InfoRecyclerViewHolder>() {
    private var status: List<Status>? = null
    constructor(status: List<Status>) : this() {
        this.status = status
    }
//    constructor()
    class InfoRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character_info, parent, false)
        return InfoRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoRecyclerViewHolder, position: Int) {
        holder.apply {
            Log.e("status ..")
            status?.get(position)?.let {
                tvStatus.text = "${it.name}: ${it.value}"
            }
        }
    }

    override fun getItemCount(): Int = status?.size!!
}