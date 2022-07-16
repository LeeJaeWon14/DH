package com.jeepchief.dh.view.character.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.database.characters.CharactersEntity

class ChangeCharacterAdapter(private val list: List<CharactersEntity>) : RecyclerView.Adapter<ChangeCharacterAdapter.ChangeCharacterViewHolder>() {
    class ChangeCharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCharacterImage: ImageView = view.findViewById(R.id.iv_character_image)
        val tvServerName: TextView = view.findViewById(R.id.tv_server_name)
        val tvNickname: TextView = view.findViewById(R.id.tv_nickname)
        val tvJob: TextView = view.findViewById(R.id.tv_job)
        val rlCharacter: RelativeLayout = view.findViewById(R.id.rl_character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeCharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_character, parent, false)
        return ChangeCharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChangeCharacterViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                Glide.with(itemView.context)
                    .load(String.format(NetworkConstants.CHARACTER_URL, serverId, characterId))
                    .error(R.drawable.ic_launcher_foreground)
                    .thumbnail(0.2f)
                    .override(400, 460)
                    .into(ivCharacterImage)

                tvServerName.text = serverId
                tvNickname.text = characterName.plus("(Lv. $level)")
                tvJob.text = jobName.plus(" - $jobGrowName")

                rlCharacter.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}