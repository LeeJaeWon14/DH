package com.jeepchief.dh.view.myinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.Avatar
import com.jeepchief.dh.util.RarityChecker

class AvatarRecyclerAdapter() : RecyclerView.Adapter<AvatarRecyclerAdapter.InfoRecyclerViewHolder>() {
    private var avatar: List<Avatar>? = null
    constructor(avatar: List<Avatar>) : this() {
        this.avatar = avatar
    }
    class InfoRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val ivEquip: ImageView = view.findViewById(R.id.iv_equip)
        val llAvatar: LinearLayout = view.findViewById(R.id.ll_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipment_info, parent, false)
        return InfoRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoRecyclerViewHolder, position: Int) {
        holder.apply {
            avatar?.get(position)?.let {
                Glide.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(ivEquip)
                tvStatus.text = it.itemName.plus( "(${it.slotName})")
                tvStatus.setTextColor(RarityChecker.convertColor(it.itemRarity))

                llAvatar.setOnClickListener { _ ->
                    val dlgView = View.inflate(itemView.context, R.layout.layout_dialog_emblems, null)
                    val dlg = AlertDialog.Builder(itemView.context).create().apply {
                        setView(dlgView)
                        setCancelable(false)
                    }

                    dlgView.run {
                        findViewById<RecyclerView>(R.id.rv_emblems).run {
                            layoutManager = LinearLayoutManager(itemView.context).apply {
                                orientation = LinearLayoutManager.HORIZONTAL
                            }
                            adapter = EmblemsAdapter(it.emblems)
                        }
                        findViewById<Button>(R.id.btn_close).run {
                            setOnClickListener { dlg.dismiss() }
                        }
                    }
                    dlg.show()
                }
            }
        }
    }

    override fun getItemCount(): Int = avatar?.size!!
}