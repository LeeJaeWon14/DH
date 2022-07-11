package com.jeepchief.dh.view.itemsearch.adapter

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
import com.jeepchief.dh.model.rest.dto.ItemRows
import com.jeepchief.dh.util.RarityChecker

class SearchResultAdapter(private val list: List<ItemRows>) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {
    class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivSearchItem: ImageView = view.findViewById(R.id.iv_search_item)
        val tvSearchItem1: TextView = view.findViewById(R.id.tv_search_item_1)
        val tvSearchItem2: TextView = view.findViewById(R.id.tv_search_item_2)
        val llItemRow: LinearLayout = view.findViewById(R.id.ll_item_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_items_row, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                Glide.with(itemView.context)
                    .load(String.format(NetworkConstants.ITEM_URL, itemId))
                    .override(112, 112)
                    .centerCrop()
                    .into(ivSearchItem)

                tvSearchItem1.apply {
                    text = itemName.plus(" (Lv. $itemAvailableLevel)")
                    setTextColor(RarityChecker.convertColor(itemRarity))
                }
                tvSearchItem2.text = itemType.plus("-$itemTypeDetail")

                llItemRow.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}