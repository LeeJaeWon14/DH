package com.jeepchief.dh.view.timeline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentTimelineBinding
import com.jeepchief.dh.model.rest.dto.TimeLineRows
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.main.fragment.BaseFragment
import com.jeepchief.dh.view.timeline.adapter.TimeLineDateAdapter
import com.jeepchief.dh.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TimelineFragment : BaseFragment() {
    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mContext: Context
    private val timeLineMap = hashMapOf<String, MutableList<TimeLineRows>>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fabBack.setOnClickListener { requireActivity().onBackPressed() }
        }

        viewModel.run {
            timeLine.observe(requireActivity()) {
                runBlocking(Dispatchers.Default) {
                    it.timeline.rows.forEach { row ->
                        timeLineMap[makeHash(row.date)]?.add(row) ?: run {
                            timeLineMap.put(makeHash(row.date), mutableListOf(row))
                        }
                    }
                    timeLineMap.keys.forEach {
                        Log.e("map is ${timeLineMap[it]}")
                    }
                }
                binding.rvTimeline.apply {
                    val manager = LinearLayoutManager(mContext)
                    layoutManager = manager
//                    adapter = TimeLineAdapter(it.timeline.rows)
                    adapter = TimeLineDateAdapter(timeLineMap)
                    addItemDecoration(DividerItemDecoration(
                        mContext, manager.orientation
                    ))
                }
            }
            getTimeLine()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun makeHash(date: String) : String = date.split(" ")[0]
}