package com.jeepchief.dh.features.myinfo.buffequip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.network.dto.BuffEquipment
import com.jeepchief.dh.core.util.RarityChecker
import com.jeepchief.dh.databinding.ItemEquipmentInfoBinding

class BuffEquipAdapter(
    private val equipment: List<BuffEquipment>,
    private val itemInfoAction: (String) -> Unit
    ) : RecyclerView.Adapter<BuffEquipAdapter.BuffEquipViewHolder>() {

    class BuffEquipViewHolder(private val binding: ItemEquipmentInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(equipment: BuffEquipment, itemInfoAction: (String) -> Unit) {
            binding.apply {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, equipment.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(ivEquip)
                tvStatus.text = equipment.itemName.plus(" +${equipment.reinforce}\r\n(${equipment.itemType} - ${equipment.itemTypeDetail})")
                tvStatus.setTextColor(RarityChecker.convertColor(equipment.itemRarity))

                llAvatar.setOnClickListener {
                    itemInfoAction(equipment.itemId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffEquipViewHolder {
        val binding = ItemEquipmentInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuffEquipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuffEquipViewHolder, position: Int) {
        holder.bind(equipment[position], itemInfoAction)
    }

    override fun getItemCount(): Int = equipment.size
}