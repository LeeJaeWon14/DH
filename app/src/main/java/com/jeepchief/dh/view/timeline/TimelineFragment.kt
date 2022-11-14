package com.jeepchief.dh.view.timeline

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentTimelineBinding
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.TimeLineDTO
import com.jeepchief.dh.model.rest.dto.TimeLineRows
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.main.fragment.BaseFragment
import com.jeepchief.dh.view.timeline.adapter.TimeLineDateAdapter
import com.jeepchief.dh.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TimelineFragment : BaseFragment() {
    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val vm: TimeLineViewModel by viewModels()
    private lateinit var mContext: Context
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
                    it.timeline.rows.forEach { row ->
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
            viewModel.mySimpleInfo.value?.let {
                getTimeLine(it.serverId, it.characterId)
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun makeHash(date: String) : String = date.split(" ")[0]
}

class TimeLineViewModel() : ViewModel() {
    private val dfService = RetroClient.getInstance().create(DfService::class.java)
    // Get Timeline
    private val _timeLine: MutableLiveData<TimeLineDTO> by lazy { MutableLiveData<TimeLineDTO>() }
    val timeLine: LiveData<TimeLineDTO> get() = _timeLine

    fun getTimeLine(serverId: String, characterId: String) {
        viewModelScope.launch {
            _timeLine.value = dfService.getTimeLine(serverId, characterId)
        }
    }
}