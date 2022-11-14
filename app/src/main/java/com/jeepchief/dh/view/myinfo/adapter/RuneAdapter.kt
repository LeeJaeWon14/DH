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
import com.jeepchief.dh.model.rest.dto.Runes

class RuneAdapter(private val runeList: MutableList<Runes>) : RecyclerView.Adapter<RuneAdapter.RuneViewHolder>() {
    class RuneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivRune: ImageView = view.findViewById(R.id.iv_rune)
        val tvRune: TextView = view.findViewById(R.id.tv_rune)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rune, parent, false)
        return RuneViewHolder(view)
    }

    override fun onBindViewHolder(holder: RuneViewHolder, position: Int) {
        holder.apply {
            runeList[position].run {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, itemId))
                    .override(112, 112)
                    .centerCrop()
                    .into(ivRune)

                tvRune.text = itemName
            }
        }
    }

    override fun getItemCount(): Int = runeList.size
}