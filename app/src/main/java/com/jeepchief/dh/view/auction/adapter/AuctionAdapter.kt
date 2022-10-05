package com.jeepchief.dh.view.auction.adapter

import android.content.Intent
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
import com.jeepchief.dh.model.rest.dto.AuctionRows
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.auction.AuctionActivity
import java.text.DecimalFormat

class AuctionAdapter(private val list: List<AuctionRows>) : RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder>() {
    class AuctionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItem = view.findViewById<ImageView>(R.id.iv_search_item)
        val tvItemName = view.findViewById<TextView>(R.id.tv_search_item_1)
        val tvItemPrice = view.findViewById<TextView>(R.id.tv_search_item_2)
        val llAuction = view.findViewById<LinearLayout>(R.id.ll_item_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_items_row, parent, false)
        return AuctionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, itemId))
                    .override(112, 112)
                    .centerCrop()
                    .into(ivItem)

                tvItemName.run {
                    text = itemName.plus(" (+$reinforce)")
                    setTextColor(RarityChecker.convertColor(itemRarity))
                }

                tvItemPrice.text = makeComma(currentPrice.toString()).plus("골드")

                llAuction.setOnClickListener {
                    val intent = Intent(itemView.context, AuctionActivity::class.java)
                    intent.putExtra("AuctionRows", this)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun makeComma(price : String) : String {
        //소숫점이 존재하거나 천 단위 이하일 경우 생략
        if(price.contains(".") || price.length < 4) {
            return price
        }
        val formatter = DecimalFormat("###,###")
        return formatter.format(price.toLong())
    }
}