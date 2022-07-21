package com.jeepchief.dh.view.itemsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentItemSearchBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.itemsearch.adapter.ItemStatusAdapter
import com.jeepchief.dh.view.itemsearch.adapter.SearchResultAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class ItemSearchFragment : Fragment() {
    private var _binding: FragmentItemSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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
                    Toast.makeText(requireContext(), "결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    return@observe
                }
                binding.rvSearchItem.apply {
                    val manager = LinearLayoutManager(requireContext())
                    layoutManager = manager
                    adapter = SearchResultAdapter(it.rows, viewModel)
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                }
            }

            itemInfo.observe(requireActivity()) {
                val dlgView = View.inflate(requireContext(), R.layout.layout_dialog_item_info, null)
                val dlg = AlertDialog.Builder(requireContext()).create().apply {
                    setView(dlgView)
                    setCancelable(false)
                }

                dlgView.run {
                    findViewById<TextView>(R.id.tv_item_name).run {
                        text = it.itemName.plus(" (Lv. ${it.itemAvailableLevel})")
                        setTextColor(RarityChecker.convertColor(it.itemRarity))
                    }
                    Glide.with(requireContext())
                        .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                        .centerCrop()
                        .override(112, 112)
                        .into(findViewById<ImageView>(R.id.iv_item_info_image))
                    findViewById<TextView>(R.id.tv_item_type).text = it.itemType.plus(" - ${it.itemTypeDetail}")
                    findViewById<TextView>(R.id.tv_item_obtain).text = it.itemObtainInfo
                    findViewById<TextView>(R.id.tv_item_explation).text = it.itemExplain
                    findViewById<TextView>(R.id.tv_item_flavor).run {
                        if(it.itemFlavorText == "") isVisible = false
                        else text = it.itemFlavorText
                    }
                    findViewById<RecyclerView>(R.id.rv_item_status).run {
                        it.itemStatus?.let {
                            val manager = LinearLayoutManager(requireContext())
                            layoutManager = manager
                            adapter = ItemStatusAdapter(it)
                            addItemDecoration(DividerItemDecoration(
                                requireContext(), manager.orientation
                            ))
                        } ?: run { isVisible = false }
                    }
                    findViewById<Button>(R.id.btn_item_info_close).setOnClickListener {
                        dlg.dismiss()
                    }
                }

                dlg.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}