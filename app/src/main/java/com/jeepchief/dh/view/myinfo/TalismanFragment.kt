package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jeepchief.dh.databinding.FragmentTalismanBinding
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.model.rest.dto.TalismanDTO
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class TalismanFragment : Fragment() {
    private var _binding: FragmentTalismanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemInfoVM: ItemInfoViewModel by viewModels()
    private lateinit var talismanDTO: TalismanDTO
    private val talismanList = mutableListOf<ItemsDTO>()
    private val runeList = mutableListOf<ItemsDTO>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTalismanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.getTalisman()
    }

    private fun observeViewModel() {
        viewModel.talisman.observe(requireActivity()) {
            it.talismans.forEach { row ->
                itemInfoVM.getItemInfo(row.talisman.itemId)
                row.runes.forEach { rune ->
                    itemInfoVM.getItemInfo(rune.itemId)
                }
            }
            talismanDTO = it
        }

        itemInfoVM.itemInfo.observe(requireActivity()) {
            when(it.itemTypeDetail) {
                "탈리스만" -> talismanList.add(it)
                "룬" -> runeList.add(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}