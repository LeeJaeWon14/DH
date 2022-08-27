package com.jeepchief.dh.view.dictionary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentDictionaryBinding
import com.jeepchief.dh.databinding.LayoutDialogSkillInfoBinding
import com.jeepchief.dh.databinding.LayoutDialogSkillsBinding
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.dictionary.adapter.JobRecyclerAdapter
import com.jeepchief.dh.view.dictionary.adapter.SkillRecyclerAdapter
import com.jeepchief.dh.view.main.fragment.SuperFragment
import com.jeepchief.dh.viewmodel.MainViewModel

class DictionaryFragment : SuperFragment() {
    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var isAttached = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAttached = true
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
        viewModel.run {
            jobs.observe(requireActivity()) {
                try {
                    binding.rvJob.apply {
                        val manager = LinearLayoutManager(requireContext())
                        layoutManager = manager
                        adapter = JobRecyclerAdapter(it.jobRows, viewModel)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
                } catch(e: Exception) {
                    Log.e("now flag is $isAttached")
                    e.printStackTrace()
                }
            }

            skills.observe(requireActivity()) { dto ->
                val dlgView = LayoutDialogSkillsBinding.inflate(layoutInflater)
                val dlg = AlertDialog.Builder(requireContext()).create().apply {
                    setView(dlgView.root)
                    setCancelable(false)
                }
                dlgView.apply {
                    tvJobsName.text = jobName.plus("의 길")
                    rvSkills.apply {
                        val manager = LinearLayoutManager(requireContext())
                        layoutManager = manager
                        adapter = SkillRecyclerAdapter(dto.skills, dlg, viewModel)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
                    btnSkillClose.setOnClickListener { dlg.dismiss() }
                }
                dlg.show()
            }

            skillInfo.observe(requireActivity()) { dto ->
                Log.e(dto.toString())

                val dlgView = LayoutDialogSkillInfoBinding.inflate(layoutInflater)
                val dlg = AlertDialog.Builder(requireContext()).create()
                dlg.apply {
                    setView(dlgView.root)
                    setCancelable(false)
                }

                dlgView.apply {
                    tvSkillInfoName.text = dto.name
                    tvSkillInfoMaxLevel.text = StringBuilder("마스터 레벨 ").append(dto.maxLevel.toString())
                    dto.consumeItem?.let {
                        tvSkillConsumeItem.run {
                            isVisible = true
                            text = it.itemName.plus(" ${it.value}개 소모")
                        }
                    }
                    tvSkillDesc.text = dto.descDetail
                    btnSkillInfoClose.setOnClickListener { dlg.dismiss() }
                }

                dlg.show()
            }
        }
    }
}