package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.Equipment

class EquipViewAdapter(private val list: List<Equipment>) : RecyclerView.Adapter<EquipViewAdapter.EquipViewHolder>(){

    class EquipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_equip_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipment_view, parent, false)
        return EquipViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipViewHolder, position: Int) {
        holder.apply {
            list[position].also {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(imageView)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}