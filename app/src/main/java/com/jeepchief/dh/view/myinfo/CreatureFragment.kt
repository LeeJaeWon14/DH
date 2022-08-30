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
import com.jeepchief.dh.databinding.FragmentCreatureBinding
import com.jeepchief.dh.databinding.LayoutDialogItemInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.itemsearch.adapter.ItemStatusAdapter
import com.jeepchief.dh.view.myinfo.adapter.CreatureAdapter
import com.jeepchief.dh.view.myinfo.adapter.FlagAdapter
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class CreatureFragment : Fragment() {
    private var _binding: FragmentCreatureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemVM: ItemInfoViewModel by viewModels()

    companion object {
        fun newInstance(page : Int) : CreatureFragment {
            val fragment = CreatureFragment()
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
        _binding = FragmentCreatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.run {
            getCreature()
            getFlag()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            creature.observe(requireActivity()) {
                binding.apply {
                    // Init creature info
                    it.creature?.let {
                        Glide.with(requireContext())
                            .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                            .override(112, 112)
                            .centerCrop()
                            .into(ivCreature)

                        tvCreature.apply {
                            text = it.itemName
                            setTextColor(RarityChecker.convertColor(it.itemRarity))
                        }
                        rvCreature.apply {
                            val manager = LinearLayoutManager(requireContext())
                            layoutManager = manager
                            adapter = CreatureAdapter(it.artifact)
                            addItemDecoration(DividerItemDecoration(
                                requireContext(), manager.orientation
                            ))
                        }
                        llCreature.setOnClickListener { _ ->
                            itemVM.getItemInfo(it.itemId)
                        }
                    } ?: run {
                        tvCreature.run {
                            text = "장착된 크리쳐가 없습니다."
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }
                    }
                }
            }
            flag.observe(requireActivity()) {
                binding.apply {
                    // Init flag info
                    it.flag?.let { // When not equipped flag.
                        Glide.with(requireContext())
                            .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                            .override(112, 112)
                            .centerCrop()
                            .into(ivFlag)

                        tvFlag.run {
                            text = it.itemName.plus("(Lv. ${it.itemAvailableLevel})")
                            setTextColor(RarityChecker.convertColor(it.itemRarity))
                        }
                        tvFlagAbility.text = it.itemAbility
                        rvFlag.apply {
                            val manager = LinearLayoutManager(requireContext())
                            layoutManager = manager
                            adapter = FlagAdapter(it.gems)
                            addItemDecoration(DividerItemDecoration(
                                requireContext(), manager.orientation
                            ))
                        }
                        llFlag.setOnClickListener { _ ->
                            itemVM.getItemInfo(it.itemId)
                        }
                    } ?: run {
                        tvFlag.run {
                            text = "장착된 휘장이 없습니다."
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }
                        tvFlagAbility.isVisible = false
                    }
                }
            }
        }

        itemVM.itemInfo.observe(requireActivity(), itemInfoObserver)
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
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                } ?: run { isVisible = false }
            }
            btnItemInfoClose.setOnClickListener { dlg.dismiss() }
        }

        dlg.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}