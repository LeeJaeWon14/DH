package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.Gems
import com.jeepchief.dh.util.RarityChecker

class FlagAdapter(private val list: List<Gems>) : RecyclerView.Adapter<FlagAdapter.FlagViewHolder>() {
    class FlagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGemName: TextView = view.findViewById(R.id.tv_gem_name)
        val tvGemAbility: TextView = view.findViewById(R.id.tv_gem_ability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gems, parent, false)
        return FlagViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlagViewHolder, position: Int) {
        holder.apply {
            list[position].run {
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

    override fun getItemCount(): Int = list.size
}