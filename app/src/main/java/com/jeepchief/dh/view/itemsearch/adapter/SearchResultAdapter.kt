package com.jeepchief.dh.view.itemsearch.adapter

import android.content.Context
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
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.viewmodel.ItemInfoViewModel

class SearchResultAdapter(private val list: List<ItemRows>, private val viewModel: ItemInfoViewModel) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {
    private lateinit var context: Context
    class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivSearchItem: ImageView = view.findViewById(R.id.iv_search_item)
        val tvSearchItem1: TextView = view.findViewById(R.id.tv_search_item_1)
        val tvSearchItem2: TextView = view.findViewById(R.id.tv_search_item_2)
        val llItemRow: LinearLayout = view.findViewById(R.id.ll_item_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_search_items_row, parent, false)
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
                    Log.e("itemId is $itemId")
                    viewModel.getItemInfo(itemId)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}