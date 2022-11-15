package com.jeepchief.dh.util

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jeepchief.dh.databinding.LayoutDialogItemInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.view.itemsearch.adapter.ItemStatusAdapter

object ItemInfoDialog {
    fun create(context: Context, it: ItemsDTO) : AlertDialog {
        val dlgView = LayoutDialogItemInfoBinding.inflate(LayoutInflater.from(context))
        val dlg = AlertDialog.Builder(context).create().apply {
            setView(dlgView.root)
            setCancelable(false)
        }

        dlgView.run {
            tvItemName.run {
                text = it.itemName.plus(" (Lv. ${it.itemAvailableLevel})")
                setTextColor(RarityChecker.convertColor(it.itemRarity))
            }
            Glide.with(context)
                .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                .centerCrop()
                .override(112, 112)
                .into(ivItemInfoImage)

            tvItemType.text = it.itemType.plus(" - ${it.itemTypeDetail}")
            tvItemObtain.text = it.itemObtainInfo
            tvItemExplation.text = it.itemExplain
            tvItemFlavor.run {
                if(it.itemFlavorText == "") isVisible = false
                else text = it.itemFlavorText
            }
            rvItemStatus.run {
                it.itemStatus?.let {
                    val manager = LinearLayoutManager(context)
                    layoutManager = manager
                    adapter = ItemStatusAdapter(it)
                    addItemDecoration(
                        DividerItemDecoration(
                            context, manager.orientation
                    )
                    )
                } ?: run { isVisible = false }
            }
            tvItemGrowInfo.run {
                it.growInfo?.let {
                    var count = 1
                    val strBuilder = StringBuilder("$count. ")
                    it.options.forEach { option ->
                        option.explainDetail?.let { explainDetail ->
                            count ++
                            if(count < 5) {
                                strBuilder.append(explainDetail.plus("\n$count. "))
                            } else {
                                strBuilder.append(explainDetail)
                            }
                        } ?: run {
                            // only custom epic item
                            strBuilder.run {
                                clear()
                                append(option.explain)
                            }
                        }

                    }
                    text = strBuilder.toString()
                } ?: run { isVisible = false }
            }
            btnItemInfoClose.setOnClickListener { dlg.dismiss() }
        }

        return dlg
    }
}