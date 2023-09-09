package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.databinding.ItemEquipmentInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.Equipment
import com.jeepchief.dh.util.GlideApp
import com.jeepchief.dh.util.RarityChecker

class EquipmentRecyclerAdapter(
    private val equipment: List<Equipment>,
    private val itemInfoAction: (String) -> Unit
) : RecyclerView.Adapter<EquipmentRecyclerAdapter.InfoRecyclerViewHolder>() {

    class InfoRecyclerViewHolder(private val binding: ItemEquipmentInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(equipment: Equipment, itemInfoAction: (String) -> Unit) {
            binding.apply {
                GlideApp.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, equipment.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(ivEquip)

                if(equipment.itemType == "무기") {
                    tvStatus.text = equipment.itemName.plus(" +${equipment.reinforce}(${equipment.refine}) (${equipment.itemType} - ${equipment.itemTypeDetail})")
                } else {
                    tvStatus.text = equipment.itemName.plus(" +${equipment.reinforce} (${equipment.itemType} - ${equipment.itemTypeDetail})")
                }

                tvStatus.setTextColor(RarityChecker.convertColor(equipment.itemRarity))

                llAvatar.setOnClickListener { _ ->
                    itemInfoAction(equipment.itemId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRecyclerViewHolder {
        val binding = ItemEquipmentInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoRecyclerViewHolder, position: Int) {
        holder.bind(equipment[position], itemInfoAction)
    }

    override fun getItemCount(): Int = equipment.size
}