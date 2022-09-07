package com.jeepchief.dh.view.character.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jeepchief.dh.R
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.database.DhDatabase
import com.jeepchief.dh.model.rest.dto.CharacterRows
import com.jeepchief.dh.model.rest.dto.ServerDTO
import com.jeepchief.dh.util.Pref
import com.jeepchief.dh.view.main.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeCharacterAdapter(
    private val _list: List<CharacterRows>,
    private val server: ServerDTO
    ) : RecyclerView.Adapter<ChangeCharacterAdapter.ChangeCharacterViewHolder>() {
    private val list = _list.sortedBy { it.level }.reversed().toMutableList()
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

                server.rows.forEach { row ->
                    if(row.serverId == serverId)
                        tvServerName.text = row.serverName
                }
//                tvServerName.text = serverId

                tvNickname.text = characterName.plus("(Lv. $level)")
                tvJob.text = jobName.plus(" - $jobGrowName")

                rlCharacter.run {
                    setOnClickListener {
                        val row = CharacterRows(serverId, characterId, characterName, level, jobId, jobGrowId, jobName, jobGrowName)
                        val rowJson = Gson().toJson(row)
                        Pref.getInstance(itemView.context)?.setValue(Pref.CHARACTER_INFO, rowJson)
                        (itemView.context as MainActivity).run {
                            updateSimpleInfo(row)
                            finishAffinity()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }

                    setOnLongClickListener {
                        AlertDialog.Builder(itemView.context)
                            .setMessage("삭제하시겠습니까?")
                            .setPositiveButton("삭제") { dialogInterface: DialogInterface, i: Int ->
                                CoroutineScope(Dispatchers.IO).launch  {
                                    DhDatabase.getInstance(itemView.context).getCharactersDAO()
                                        .deleteCharacter(characterId)
//                                    list.removeAt(position)
                                    withContext(Dispatchers.Main) {
                                        val newList = mutableListOf<CharacterRows>().apply {
                                            addAll(list)
                                            removeAt(position)
                                        }
                                        updateList(newList)
                                    }
                                }

                            }
                            .setNegativeButton("취소", null)
                            .show()
                        false
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun updateList(newList: List<CharacterRows>) {
        val diffCallback = CharacterDiffUtilCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class CharacterDiffUtilCallback(
        private val oldList: List<CharacterRows>,
        private val newList: List<CharacterRows>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].characterId == newList[newItemPosition].characterId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}