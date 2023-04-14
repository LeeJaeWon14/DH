package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.databinding.ItemRuneBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.Runes

class RuneAdapter(private val runeList: MutableList<Runes>) : RecyclerView.Adapter<RuneAdapter.RuneViewHolder>() {
    class RuneViewHolder(private val binding: ItemRuneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rune: Runes) {
            binding.apply {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, rune.itemId))
                    .override(112, 112)
                    .centerCrop()
                    .into(ivRune)

                tvRune.text = rune.itemName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuneViewHolder {
        val binding = ItemRuneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RuneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RuneViewHolder, position: Int) {
        holder.bind(runeList[position])
    }

    override fun getItemCount(): Int = runeList.size
}