package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.LayoutDialogItemInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.RarityChecker

class RuneAdapter(private val runeList: MutableList<ItemsDTO>) : RecyclerView.Adapter<RuneAdapter.RuneViewHolder>() {
    class RuneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivRune: ImageView = view.findViewById(R.id.iv_rune)
        val tvRune: TextView = view.findViewById(R.id.tv_rune)
        val llRune: LinearLayout = view.findViewById(R.id.ll_rune)
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

                tvRune.run {
                    text = itemName
                    setTextColor(RarityChecker.convertColor(itemRarity))
                }

                llRune.setOnClickListener {
                    val dlgView = LayoutDialogItemInfoBinding.inflate(LayoutInflater.from(itemView.context))
                    val dlg = AlertDialog.Builder(itemView.context).create().apply {
                        setView(dlgView.root)
                        setCancelable(false)
                    }

                    dlgView.run {
                        tvItemName.run {
                            text = itemName.plus(" (Lv. ${itemAvailableLevel})")
                            setTextColor(RarityChecker.convertColor(itemRarity))
                        }
                        Glide.with(itemView.context)
                            .load(String.format(NetworkConstants.ITEM_URL, itemId))
                            .centerCrop()
                            .override(112, 112)
                            .into(ivItemInfoImage)

                        tvItemType.text = itemType.plus(" - ${itemTypeDetail}")
                        tvItemObtain.text = itemObtainInfo
                        tvItemExplation.text = itemExplain
                        tvItemFlavor.run {
                            if (itemFlavorText == "") isVisible = false
                            else text = itemFlavorText
                        }
                        btnItemInfoClose.setOnClickListener { dlg.dismiss() }
                    }

                    dlg.show()
                }
            }
        }
    }

    override fun getItemCount(): Int = runeList.size
}