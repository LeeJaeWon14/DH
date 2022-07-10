package com.jeepchief.dh.view.myinfo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.Artifact
import com.jeepchief.dh.util.RarityChecker

class CreatureAdapter(private val list: List<Artifact>) : RecyclerView.Adapter<CreatureAdapter.CreatureViewHolder>() {
    class CreatureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvArtifactColor: TextView = view.findViewById(R.id.tv_artifact_color)
        val tvArtifactName: TextView = view.findViewById(R.id.tv_artifact_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artifact, parent, false)
        return CreatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreatureViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvArtifactColor.apply {
                    text = slotColor.plus(" Artifact")
                    when(slotColor) {
                        "RED" -> setTextColor(Color.RED)
                        "BLUE" -> setTextColor(Color.BLUE)
                        "GREEN" -> setTextColor(Color.GREEN)
                    }
                }
                tvArtifactName.apply {
                    text = itemName
                    setTextColor(RarityChecker.convertColor(itemRarity))
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}