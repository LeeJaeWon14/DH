package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.Emblems
import com.jeepchief.dh.util.RarityChecker

class EmblemsAdapter(private val list: List<Emblems>) : RecyclerView.Adapter<EmblemsAdapter.EmblemsViewHolder>() {
    class EmblemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSlot: TextView = view.findViewById(R.id.tv_slot)
        val tvSlotColor: TextView = view.findViewById(R.id.tv_slot_color)
        val tvEmblemName: TextView = view.findViewById(R.id.tv_emblem_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmblemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emblems, parent, false)
        return EmblemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmblemsViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvSlot.text = slotNo.toString().plus("번 슬롯")
                tvSlotColor.text = slotColor
                tvEmblemName.text = itemName
                tvEmblemName.setTextColor(RarityChecker.convertColor(itemRarity))
            }
        }
    }

    override fun getItemCount(): Int = list.size
}