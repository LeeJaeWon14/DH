package com.jeepchief.dh.view.myinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.ItemEquipmentInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.Avatar
import com.jeepchief.dh.util.GlideApp
import com.jeepchief.dh.util.RarityChecker

class AvatarRecyclerAdapter(private val avatar: List<Avatar>) : RecyclerView.Adapter<AvatarRecyclerAdapter.InfoRecyclerViewHolder>() {
    private lateinit var context: Context
    class InfoRecyclerViewHolder(private val binding: ItemEquipmentInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(avatar: Avatar, context: Context) {
            binding.apply {
                GlideApp.with(itemView)
                    .load(String.format(NetworkConstants.ITEM_URL, avatar.itemId))
                    .centerCrop()
                    .override(112, 112)
                    .into(ivEquip)
                tvStatus.text = avatar.itemName.plus( "(${avatar.slotName})")
                tvStatus.setTextColor(RarityChecker.convertColor(avatar.itemRarity))

                avatar.clone.itemId?.let { itemId ->
                    llClone.isVisible = true
                    GlideApp.with(itemView)
                        .load(String.format(NetworkConstants.ITEM_URL, itemId))
                        .centerCrop()
                        .override(112, 112)
                        .into(ivClone)
                    tvCloneName.text = avatar.clone.itemName
                }

                avatar.random?.let { random ->
                    llRandom.isVisible = true
                    GlideApp.with(itemView)
                        .load(String.format(NetworkConstants.ITEM_URL, random.itemId))
                        .centerCrop()
                        .override(112, 112)
                        .into(ivRandom)
                    tvRandomName.text = random.itemName
                }

                llAvatar.setOnClickListener { _ ->
                    if(avatar.emblems.isEmpty()) {
                        Toast.makeText(context, "장착된 엠블럼 없음", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
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
                            adapter = EmblemsAdapter(avatar.emblems)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRecyclerViewHolder {
        context = parent.context
        val binding = ItemEquipmentInfoBinding.inflate(LayoutInflater.from(context), parent, false)
        return InfoRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoRecyclerViewHolder, position: Int) {
        holder.bind(avatar[position], context)
    }

    override fun getItemCount(): Int = avatar.size
}