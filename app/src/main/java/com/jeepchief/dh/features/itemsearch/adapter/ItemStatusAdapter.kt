package com.jeepchief.dh.features.itemsearch.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.core.network.dto.ItemStatus
import com.jeepchief.dh.databinding.ItemGemsBinding

class ItemStatusAdapter(private val list: List<ItemStatus>) : RecyclerView.Adapter<ItemStatusAdapter.ItemStatusViewHolder>() {

    class ItemStatusViewHolder(private val binding: ItemGemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemStatus: ItemStatus) {
            itemStatus.run {
                tvName.text = name
                tvValueL.text = value
            }
        }
        private val tvName: TextView = binding.tvGemName.apply { setTextColor(Color.WHITE) }
        private val tvValueL: TextView = binding.tvGemAbility.apply { setTextColor(Color.WHITE) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemStatusViewHolder {
        val binding = ItemGemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemStatusViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}