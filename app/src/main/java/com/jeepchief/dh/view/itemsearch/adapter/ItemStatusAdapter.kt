package com.jeepchief.dh.view.itemsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.ItemStatus

class ItemStatusAdapter(private val list: List<ItemStatus>) : RecyclerView.Adapter<ItemStatusAdapter.ItemStatusViewHolder>() {
    class ItemStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_gem_name)
        val tvValueL: TextView = view.findViewById(R.id.tv_gem_ability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemStatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gems, parent, false)
        return ItemStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemStatusViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvName.text = name
                tvValueL.text = value
            }
        }
    }

    override fun getItemCount(): Int = list.size
}