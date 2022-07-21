package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentEquipBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.itemsearch.adapter.ItemStatusAdapter
import com.jeepchief.dh.view.myinfo.adapter.EquipmentRecyclerAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class EquipmentFragment : Fragment() {
    private var _binding: FragmentEquipBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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
        Log.e("${this.javaClass.name} is Created")

        observeViewModel()
        viewModel.getEquipment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.run {
            equipment.observe(requireActivity()) {
                binding.apply {
                    val manager = LinearLayoutManager(requireContext())
                    rvInfoList.apply {
                        layoutManager = manager
                        adapter = EquipmentRecyclerAdapter(it.equipment, viewModel)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
//                    rvInfoList.adapter = EquipmentRecyclerAdapter(it.equipment)
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
}