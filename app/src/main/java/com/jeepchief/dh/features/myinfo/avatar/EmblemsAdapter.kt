package com.jeepchief.dh.features.myinfo.avatar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.dto.Emblems
import com.jeepchief.dh.core.util.RarityChecker

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
                tvSlotColor.run {
                    text = slotColor.plus(" 슬롯")
                    setTextColor(convertSlotColor(slotColor))
                }
                tvEmblemName.text = itemName
                tvEmblemName.setTextColor(RarityChecker.convertColor(itemRarity))
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun convertSlotColor(slotColor: String) : Int = when(slotColor) {
        "붉은빛" -> Color.parseColor("#b31a21")
        "푸른빛" -> Color.parseColor("#2077a1")
        "노란빛" -> Color.parseColor("#ffa71f")
        "녹색빛" -> Color.parseColor("#4dea0d")
        "다색" -> Color.parseColor("#000000")
        "플래티넘" -> Color.parseColor("#ff6c37")
        else -> { 0 }
    }
}