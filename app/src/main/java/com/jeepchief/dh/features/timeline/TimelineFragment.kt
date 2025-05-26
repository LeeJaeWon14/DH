package com.jeepchief.dh.features.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.core.network.dto.TimeLineRows
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.databinding.FragmentTimelineBinding
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.fragment.BaseFragment
import com.jeepchief.dh.features.timeline.adapter.TimeLineDateAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TimelineFragment : BaseFragment() {
    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val vm: TimeLineViewModel by viewModels()
    private val timeLineMap = hashMapOf<String, MutableList<TimeLineRows>>()
    private var isCreated = false
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

        vm.run {
            timeLine.observe(requireActivity()) {
                Log.e("TimeLineDTO is $it")
                runBlocking(Dispatchers.Default) {
                    it.timeline!!.rows.forEach { row ->
                        timeLineMap[makeHash(row.date)]?.add(row) ?: run {
                            timeLineMap.put(makeHash(row.date), mutableListOf(row))
                        }
                    }
                }
                binding.rvTimeline.apply {
                    if(isCreated) return@observe
                    else isCreated = true
                    val manager = LinearLayoutManager(mContext)
                    layoutManager = manager
                    adapter = TimeLineDateAdapter(timeLineMap)
                    addItemDecoration(DividerItemDecoration(
                        mContext, manager.orientation
                    ))
                }
            }
            viewModel.nowCharacterInfo.value?.let {
                getTimeLine(it.serverId, it.characterId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun makeHash(date: String) : String = date.split(" ")[0]
}