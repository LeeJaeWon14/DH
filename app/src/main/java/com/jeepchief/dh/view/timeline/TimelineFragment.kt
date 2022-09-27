package com.jeepchief.dh.view.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentTimelineBinding
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.main.fragment.BaseFragment
import com.jeepchief.dh.view.timeline.adapter.TimeLineAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class TimelineFragment : BaseFragment() {
    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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
                Log.e("dto is $it")

                it.timeline.rows.forEach { row ->
                    Log.e("${row.name} >> ${row.data}")
                }

                binding.rvTimeline.apply {
                    val manager = LinearLayoutManager(requireContext())
                    layoutManager = manager
                    adapter = TimeLineAdapter(it.timeline.rows)
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                }
            }
            getTimeLine()
        }
    }
}