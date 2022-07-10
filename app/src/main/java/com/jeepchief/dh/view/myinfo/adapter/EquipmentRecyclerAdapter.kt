package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.Equipment
import com.jeepchief.dh.model.rest.dto.Status
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.RarityChecker

class EquipmentRecyclerAdapter() : RecyclerView.Adapter<EquipmentRecyclerAdapter.InfoRecyclerViewHolder>() {
    private var equipment: List<Equipment>? = null
    constructor(equipment: List<Equipment>) : this() {
        this.equipment = equipment
    }
    class InfoRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val ivEquip: ImageView = view.findViewById(R.id.iv_equip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipment_info, parent, false)
        return InfoRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoRecyclerViewHolder, position: Int) {
        holder.apply {
            equipment?.get(position)?.let {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(ivEquip)
                tvStatus.text = it.itemName.plus("+${it.reinforce} (${it.itemType} - ${it.itemTypeDetail})")
                tvStatus.setTextColor(RarityChecker.convertColor(it.itemRarity))
            }
        }
    }

    override fun getItemCount(): Int = equipment?.size!!
}