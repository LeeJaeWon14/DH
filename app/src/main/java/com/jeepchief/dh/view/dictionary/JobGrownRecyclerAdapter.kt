package com.jeepchief.dh.view.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.SubRows

class JobGrownRecyclerAdapter(private val list: List<SubRows>) : RecyclerView.Adapter<JobGrownRecyclerAdapter.JobGrownViewHolder>() {
    class JobGrownViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFirstGrown: TextView = view.findViewById(R.id.tv_first_grown)
        val tvSecondGrown: TextView = view.findViewById(R.id.tv_second_grown)
        val tvThirdGrown: TextView = view.findViewById(R.id.tv_third_grown)
        val tvFourthGrown: TextView = view.findViewById(R.id.tv_fourth_grown)
        val llJobGrown: LinearLayout = view.findViewById(R.id.ll_job_grown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobGrownViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_job_grown, parent, false)
        return JobGrownViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobGrownViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvFirstGrown.text = jobGrowName
                tvSecondGrown.text = next?.jobGrowName
                tvThirdGrown.text = next?.next?.jobGrowName
                tvFourthGrown.text = next?.next?.next?.jobGrowName
                
                llJobGrown.setOnClickListener {
                    Toast.makeText(itemView.context, "Not implemented yet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}