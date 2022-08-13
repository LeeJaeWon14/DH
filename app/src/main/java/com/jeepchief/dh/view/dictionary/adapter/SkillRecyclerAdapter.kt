package com.jeepchief.dh.view.dictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.Skills
import com.jeepchief.dh.viewmodel.MainViewModel

class SkillRecyclerAdapter(private val list: List<Skills>, private val viewModel: MainViewModel) : RecyclerView.Adapter<SkillRecyclerAdapter.SkillViewHolder>() {
    class SkillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSkillName: TextView = view.findViewById(R.id.tv_skill_name)
        val tvSkillType: TextView = view.findViewById(R.id.tv_skill_type)
        val tvSkillCost: TextView = view.findViewById(R.id.tv_skill_cost)
        val llSkills: LinearLayout = view.findViewById(R.id.ll_skills)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skills_info, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvSkillName.text = name.plus(" (Lv.${requiredLevel})")
                tvSkillType.text = convertSkillType(type)
                tvSkillCost.text = costType

                llSkills.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "Not implemented yet",
                        Toast.LENGTH_SHORT
                    ).show()


                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun convertSkillType(type: String) : String {
        return when(type) {
            "active" -> "액티브"
            else -> "패시브"
        }
    }
}