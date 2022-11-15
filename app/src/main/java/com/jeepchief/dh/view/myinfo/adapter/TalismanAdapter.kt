package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.model.rest.dto.Runes
import com.jeepchief.dh.util.ItemInfoDialog
import com.jeepchief.dh.util.RarityChecker

class TalismanAdapter(
    private val talismanList: MutableList<ItemsDTO>,
    private val talismanMap : MutableMap<String, MutableList<Runes>>
    ) : RecyclerView.Adapter<TalismanAdapter.TalismanViewHolder>() {
    class TalismanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTalisman: TextView = view.findViewById(R.id.tv_talisman)
        val ivTalisman: ImageView = view.findViewById(R.id.iv_talisman)
        val rvRune: RecyclerView = view.findViewById(R.id.rv_rune)
        val llTalisman: LinearLayout = view.findViewById(R.id.ll_talisman)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalismanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_talisman, parent, false)
        return TalismanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TalismanViewHolder, position: Int) {
        holder.apply {
            talismanList[position].run {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, itemId))
                    .override(112, 112)
                    .centerCrop()
                    .into(ivTalisman)

                tvTalisman.run {
                    text = itemName
                    setTextColor(RarityChecker.convertColor(itemRarity))
                }

                rvRune.apply {
                    val manager = LinearLayoutManager(itemView.context)
                    layoutManager = manager
                    addItemDecoration(DividerItemDecoration(
                        itemView.context, manager.orientation
                    ))
                    talismanMap[itemName]?.let {
                        adapter = RuneAdapter(it)
                    }
                }

                llTalisman.setOnClickListener {
                    ItemInfoDialog.create(itemView.context, this).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = talismanList.size
}