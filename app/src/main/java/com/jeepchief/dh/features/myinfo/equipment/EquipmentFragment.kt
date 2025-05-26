package com.jeepchief.dh.features.myinfo.equipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.core.network.dto.EquipmentDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.util.DialogHelper
import com.jeepchief.dh.databinding.FragmentEquipBinding
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.fragment.ContextFragment
import com.jeepchief.dh.features.myinfo.ItemInfoViewModel

class EquipmentFragment : ContextFragment() {
    private var _binding: FragmentEquipBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemInfoVM: ItemInfoViewModel by viewModels()
//    private lateinit var mContext: Context

    companion object {
        fun newInstance(page : Int) : EquipmentFragment {
            val fragment = EquipmentFragment()
            val args = Bundle()
            args.putInt("page", page)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEquipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.getEquipment()
    }

    override fun onPause() {
        super.onPause()
        viewModel.equipment.removeObserver(equipmentObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.run {
            equipment.observe(requireActivity(), equipmentObserver)
        }

        itemInfoVM.itemInfo.observe(requireActivity(), itemInfoObserver)
    }

    private var equipmentObserver = Observer<EquipmentDTO> {
        try {
            binding.apply {
                val manager = LinearLayoutManager(requireContext())
                rvInfoList.apply {
                    layoutManager = manager
                    adapter = EquipmentRecyclerAdapter(it.equipment) { itemId ->
                        itemInfoVM.getItemInfo(itemId)
                    }
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                }
            }
        } catch(e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private var itemInfoObserver = Observer<ItemsDTO> {
        DialogHelper.itemInfoDialog(mContext, it).show()
    }
}