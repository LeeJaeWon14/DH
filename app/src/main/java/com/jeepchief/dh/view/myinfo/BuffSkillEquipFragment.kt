package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jeepchief.dh.databinding.FragmentBuffSkillEquipBinding
import com.jeepchief.dh.databinding.LayoutDialogItemInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.BuffEquipDTO
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.itemsearch.adapter.ItemStatusAdapter
import com.jeepchief.dh.view.myinfo.adapter.BuffEquipAdapter
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class BuffSkillEquipFragment : Fragment() {
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
        binding.run {
            rvBuffSkillEquip.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = BuffEquipAdapter(it.skill.buff.equipment, itemInfoVM)
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
    }

    private val runnable = Runnable {
        Log.e("{value1}")
    }

    private var itemInfoObserver = Observer<ItemsDTO> {
        val dlgView = LayoutDialogItemInfoBinding.inflate(LayoutInflater.from(requireContext()))
        val dlg = AlertDialog.Builder(requireContext()).create().apply {
            setView(dlgView.root)
            setCancelable(false)
        }

        dlgView.run {
            tvItemName.run {
                text = it.itemName.plus(" (Lv. ${it.itemAvailableLevel})")
                setTextColor(RarityChecker.convertColor(it.itemRarity))
            }
            Glide.with(requireContext())
                .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                .centerCrop()
                .override(112, 112)
                .into(ivItemInfoImage)

            tvItemType.text = it.itemType.plus(" - ${it.itemTypeDetail}")
            tvItemObtain.text = it.itemObtainInfo
            tvItemExplation.text = it.itemExplain
            tvItemFlavor.run {
                if(it.itemFlavorText == "") isVisible = false
                else text = it.itemFlavorText
            }
            rvItemStatus.run {
                it.itemStatus?.let {
                    val manager = LinearLayoutManager(requireContext())
                    layoutManager = manager
                    adapter = ItemStatusAdapter(it)
                    addItemDecoration(
                        DividerItemDecoration(
                        requireContext(), manager.orientation
                    )
                    )
                } ?: run { isVisible = false }
            }
            btnItemInfoClose.setOnClickListener { dlg.dismiss() }
        }

        dlg.show()
    }
}