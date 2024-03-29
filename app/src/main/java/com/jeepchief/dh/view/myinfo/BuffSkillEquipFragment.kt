package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentBuffSkillEquipBinding
import com.jeepchief.dh.model.rest.dto.BuffEquipDTO
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.DialogHelper
import com.jeepchief.dh.view.main.fragment.ContextFragment
import com.jeepchief.dh.view.myinfo.adapter.BuffEquipAdapter
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class BuffSkillEquipFragment : ContextFragment() {
    private var _binding: FragmentBuffSkillEquipBinding? = null
    private val binding get()= _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private val itemInfoVM by viewModels<ItemInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuffSkillEquipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.getBuffSkillEquip()
    }

    private fun observeViewModel() {
        viewModel.buffSkillEquip.observe(requireActivity(), buffEquipObserver)
        itemInfoVM.itemInfo.observe(requireActivity(), itemInfoObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val buffEquipObserver = Observer<BuffEquipDTO> {
        try {
            binding.run {
                rvBuffSkillEquip.apply {
                    val manager = LinearLayoutManager(requireContext())
                    layoutManager = manager
                    adapter = BuffEquipAdapter(it.skill.buff.equipment) { itemId ->
                        itemInfoVM.getItemInfo(itemId)
                    }
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                }

                it.skill.buff.skillInfo.run {
                    tvBuffName.text = name.plus(" + ${option.level}")
                    option.desc.also { desc ->
                        var str = desc
                        for(i in 1 .. option.values.size) {
                            str = str.replace("{value$i}", option.values[i-1])
                        }
                        tvBuffDesc.text = str
                    }
                }
            }
        } catch (e: Exception) {
//            Toast.makeText(mContext, "null", Toast.LENGTH_SHORT).show()
        }
    }

    private var itemInfoObserver = Observer<ItemsDTO> {
        DialogHelper.itemInfoDialog(mContext, it).show()
    }
}