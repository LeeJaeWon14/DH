package com.jeepchief.dh.features.myinfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.core.network.dto.Gems
import com.jeepchief.dh.core.util.RarityChecker
import com.jeepchief.dh.databinding.ItemGemsBinding

class FlagAdapter(private val list: List<Gems>) : RecyclerView.Adapter<FlagAdapter.FlagViewHolder>() {

    class FlagViewHolder(private val binding: ItemGemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gem: Gems) {
            binding.apply {
                gem.run {
                    tvGemName.apply {
                        text = itemName
                        setTextColor(RarityChecker.convertColor(itemRarity))
                    }
                    tvGemAbility.apply {
                        text = itemAbility
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val binding = ItemGemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlagViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}