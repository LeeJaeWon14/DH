package com.jeepchief.dh.view.dictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.R
import com.jeepchief.dh.model.rest.dto.JobRows
import com.jeepchief.dh.viewmodel.MainViewModel

class JobRecyclerAdapter(private val _list: List<JobRows>, private val viewModel: MainViewModel) : RecyclerView.Adapter<JobRecyclerAdapter.JobViewHolder>() {
    private val list get() = _list.sortedBy { it.jobName }
    class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJobName: TextView = view.findViewById(R.id.tv_job_name)
        val rvJobGrown: RecyclerView = view.findViewById(R.id.rv_job_grown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.apply {
            list[position].run {
                tvJobName.text = jobName
                rvJobGrown.apply {
                    val manager = LinearLayoutManager(itemView.context)
                    layoutManager = manager
                    adapter = JobGrownRecyclerAdapter(subRows, viewModel, jobId)
                    addItemDecoration(DividerItemDecoration(
                        itemView.context, manager.orientation
                    ))
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}