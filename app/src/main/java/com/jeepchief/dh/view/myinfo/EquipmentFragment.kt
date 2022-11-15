package com.jeepchief.dh.view.myinfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentEquipBinding
import com.jeepchief.dh.model.rest.dto.EquipmentDTO
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.ItemInfoDialog
import com.jeepchief.dh.view.myinfo.adapter.EquipmentRecyclerAdapter
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class EquipmentFragment : Fragment() {
    private var _binding: FragmentEquipBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemInfoVM: ItemInfoViewModel by viewModels()
    private lateinit var mContext: Context

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
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
                    adapter = EquipmentRecyclerAdapter(it.equipment, itemInfoVM)
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                }
            }
        } catch(e: NullPointerException) {
            e.printStackTrace()
//            Toast.makeText(mContext, "null", Toast.LENGTH_SHORT).show()
        }
    }

    private var itemInfoObserver = Observer<ItemsDTO> {
//        val dlgView = LayoutDialogItemInfoBinding.inflate(LayoutInflater.from(requireContext()))
//        val dlg = AlertDialog.Builder(requireContext()).create().apply {
//            setView(dlgView.root)
//            setCancelable(false)
//        }
//
//        dlgView.run {
//            tvItemName.run {
//                text = it.itemName.plus(" (Lv. ${it.itemAvailableLevel})")
//                setTextColor(RarityChecker.convertColor(it.itemRarity))
//            }
//            Glide.with(requireContext())
//                .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
//                .centerCrop()
//                .override(112, 112)
//                .into(ivItemInfoImage)
//
//            tvItemType.text = it.itemType.plus(" - ${it.itemTypeDetail}")
//            tvItemObtain.text = it.itemObtainInfo
//            tvItemExplation.text = it.itemExplain
//            tvItemFlavor.run {
//                if(it.itemFlavorText == "") isVisible = false
//                else text = it.itemFlavorText
//            }
//            rvItemStatus.run {
//                it.itemStatus?.let {
//                    val manager = LinearLayoutManager(requireContext())
//                    layoutManager = manager
//                    adapter = ItemStatusAdapter(it)
//                    addItemDecoration(DividerItemDecoration(
//                        requireContext(), manager.orientation
//                    ))
//                } ?: run { isVisible = false }
//            }
//            tvItemGrowInfo.run {
//                it.growInfo?.let {
//                    var count = 1
//                    val strBuilder = StringBuilder("$count. ")
//                    it.options.forEach { option ->
//                        count ++
//                        if(count < 5) {
//                            strBuilder.append(option.explainDetail.plus("\n$count. "))
//                        } else {
//                            strBuilder.append(option.explainDetail)
//                        }
//                    }
//                    text = strBuilder.toString()
//                } ?: run { isVisible = false }
//            }
//            btnItemInfoClose.setOnClickListener { dlg.dismiss() }
//        }

//        dlg.show()

        ItemInfoDialog.create(mContext, it).show()
    }
}