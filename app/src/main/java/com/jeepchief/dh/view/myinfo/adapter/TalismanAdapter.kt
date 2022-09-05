package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.Talismans

class TalismanAdapter(private val list: List<Talismans>) : RecyclerView.Adapter<TalismanAdapter.TalismanViewHolder>() {
    class TalismanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTalisman: TextView = view.findViewById(R.id.tv_talisman)
        val ivTalisman: ImageView = view.findViewById(R.id.iv_talisman)
        val rvRune: RecyclerView = view.findViewById(R.id.rv_rune)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalismanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_talisman, parent, false)
        return TalismanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TalismanViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}