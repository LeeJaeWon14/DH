package com.jeepchief.dh.features.myinfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.core.network.dto.Status
import com.jeepchief.dh.databinding.ItemCharacterInfoBinding

class InfoRecyclerAdapter(private var status: List<Status>) : RecyclerView.Adapter<InfoRecyclerAdapter.InfoRecyclerViewHolder>() {
    class InfoRecyclerViewHolder(private val binding: ItemCharacterInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(status: Status) {
            binding.apply {
                tvStatus.text = "${status.name}: ${status.value}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRecyclerViewHolder {
        val binding = ItemCharacterInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoRecyclerViewHolder, position: Int) {
        holder.bind(status[position])
    }

    override fun getItemCount(): Int = status?.size!!
}