package com.jeepchief.dh.view.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentDictionaryBinding
import com.jeepchief.dh.viewmodel.MainViewModel

class DictionaryFragment : Fragment() {
    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.getJobs()

        // init Ui
        binding.apply {
            fabBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.jobs.observe(requireActivity()) {
            binding.rvJob.apply {
                val manager = LinearLayoutManager(requireContext())
                layoutManager = manager
                adapter = JobRecyclerAdapter(it.jobRows)
                addItemDecoration(DividerItemDecoration(
                    requireContext(), manager.orientation
                ))
            }
        }
    }
}