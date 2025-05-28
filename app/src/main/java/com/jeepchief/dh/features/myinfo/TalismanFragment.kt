package com.jeepchief.dh.features.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.network.dto.Runes
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.databinding.FragmentTalismanBinding
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.fragment.ContextFragment
import com.jeepchief.dh.features.myinfo.adapter.TalismanAdapter

class TalismanFragment : ContextFragment() {
    private var _binding: FragmentTalismanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemInfoVM: ItemInfoViewModel by viewModels()
    private lateinit var talismanDTO: TalismanDTO
    private val talismanList = mutableListOf<ItemsDTO>()
    private val talismanMap = mutableMapOf<String, MutableList<Runes>>()
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

    private var talismanCount = 0
    private fun observeViewModel() {
//        viewModel.talisman.observe(requireActivity()) {
//            try {
//                it.talismans.forEach { row ->
//                    Log.e("row is >> ${row.talisman.itemName}")
//                    itemInfoVM.getItemInfo(row.talisman.itemId)
//                    row.runes.forEach { rune ->
//                        talismanMap[row.talisman.itemName]?.add(rune) ?: run {
//                            talismanMap.put(row.talisman.itemName, mutableListOf(rune))
//                        }
//                    }
//                }
//                talismanCount = it.talismans.size
//                talismanDTO = it
//            } catch (e: Exception) {
////                Toast.makeText(requireContext(), "Null..", Toast.LENGTH_SHORT).show()
//            }
//        }

        var runeMemory = ""
        itemInfoVM.itemInfo.observe(requireActivity()) { dto ->
            when(dto.itemTypeDetail) {
                "탈리스만" -> talismanList.add(dto)
            }
            if(talismanList.size == talismanCount) {
                binding.rvTalisman.apply {
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = TalismanAdapter(talismanList, talismanMap)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}