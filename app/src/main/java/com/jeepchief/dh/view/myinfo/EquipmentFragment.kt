package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.dh.databinding.FragmentEquipBinding
import com.jeepchief.dh.databinding.FragmentInfoBinding
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.myinfo.adapter.EquipmentRecyclerAdapter
import com.jeepchief.dh.view.myinfo.adapter.InfoRecyclerAdapter
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
                        adapter = EquipmentRecyclerAdapter(it.equipment)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
//                    rvInfoList.adapter = EquipmentRecyclerAdapter(it.equipment)
                }
            }
        }
    }
}