package com.jeepchief.dh.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.CharacterRows
import com.jeepchief.dh.model.rest.dto.ServerDTO
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.Pref
import com.jeepchief.dh.view.main.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectCharacterAdapter(
    private val _list: List<CharacterRows>,
    private val dialog: AlertDialog,
    private val server: ServerDTO
    ) : RecyclerView.Adapter<SelectCharacterAdapter.SelectCharacterViewHolder>() {
    private val list get()= _list.sortedBy { it.level }.reversed()
    class SelectCharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCharacterImage: ImageView = view.findViewById(R.id.iv_character_image)
        val tvServer: TextView = view.findViewById(R.id.tv_server_name)
        val tvNickname: TextView = view.findViewById(R.id.tv_nickname)
        val tvJob: TextView = view.findViewById(R.id.tv_job)
        val rlCharacter: RelativeLayout = view.findViewById(R.id.rl_character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_character, parent, false)
        return SelectCharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectCharacterViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(itemView.context)
                        .load(String.format(NetworkConstants.CHARACTER_URL, serverId, characterId))
                        .error(R.drawable.ic_launcher_foreground)
                        .thumbnail(0.2f)
                        .override(400, 460)
                        .into(ivCharacterImage)
                }

                server.rows.forEach { row ->
                    if(row.serverId == serverId)
                        tvServer.text = row.serverName
                }

                tvNickname.text = characterName.plus("(Lv. $level)")
                tvJob.text = jobName.plus(" - $jobGrowName")

                rlCharacter.setOnClickListener {
                    Log.e("clicked character")
                    val rowJson = Gson().toJson(this)
                    Pref.getInstance(itemView.context)?.setValue(Pref.CHARACTER_INFO, rowJson)
                    (itemView.context as MainActivity).updateSimpleInfo(this)
                    dialog.dismiss()
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

}