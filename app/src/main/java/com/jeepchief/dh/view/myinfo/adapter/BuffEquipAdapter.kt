package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.BuffEquipment
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.viewmodel.ItemInfoViewModel

class BuffEquipAdapter(
    private val equipment: List<BuffEquipment>,
    private val viewModel: ItemInfoViewModel
    ) : RecyclerView.Adapter<BuffEquipAdapter.BuffEquipViewHolder>() {
    class BuffEquipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val ivEquip: ImageView = view.findViewById(R.id.iv_equip)
        val llEquip: LinearLayout = view.findViewById(R.id.ll_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffEquipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipment_info, parent, false)
        return BuffEquipViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuffEquipViewHolder, position: Int) {
        holder.apply {
            equipment[position].also {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(ivEquip)
                tvStatus.text = it.itemName.plus(" +${it.reinforce}\r\n(${it.itemType} - ${it.itemTypeDetail})")
                tvStatus.setTextColor(RarityChecker.convertColor(it.itemRarity))

                llEquip.setOnClickListener { _ ->
                    viewModel.getItemInfo(it.itemId)
                }
            }
        }
    }

    override fun getItemCount(): Int = equipment.size
}