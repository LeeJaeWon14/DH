package com.jeepchief.dh.view.itemsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentItemSearchBinding
import com.jeepchief.dh.databinding.LayoutDialogItemInfoBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.itemsearch.adapter.ItemStatusAdapter
import com.jeepchief.dh.view.itemsearch.adapter.SearchResultAdapter
import com.jeepchief.dh.view.main.fragment.SuperFragment
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class ItemSearchFragment : SuperFragment() {
    private var _binding: FragmentItemSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemInfoVM: ItemInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            observeViewModel()
        } catch(e: Exception) {
            e.printStackTrace()
            binding.btnSearchItem.performClick()
        }
        binding.apply {
            btnSearchItem.setOnClickListener {
//                val encode = URLEncoder.encode(edtSearchItem.text.toString(), "UTF-8").also { Log.e(it) }
                viewModel.getSearchItems(edtSearchItem.text.toString())
            }
            fabBack.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            rvSearchItem.adapter = null
            edtSearchItem.setText("")
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            itemsSearch.observe(requireActivity()) {
                if(it.rows.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.error_msg_no_result), Toast.LENGTH_SHORT).show()
                    return@observe
                }
                try {
                    binding.rvSearchItem.apply {
                        val manager = LinearLayoutManager(requireContext())
                        layoutManager = manager
                        adapter = SearchResultAdapter(it.rows, itemInfoVM)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), getString(R.string.error_msg_retry), Toast.LENGTH_SHORT).show()
                }
            }
        }

        itemInfoVM.itemInfo.observe(requireActivity()) {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun test(s: String) { Log.e(s) }
}