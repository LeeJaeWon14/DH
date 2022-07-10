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
import com.jeepchief.dh.databinding.FragmentInfoBinding
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.myinfo.adapter.EquipmentRecyclerAdapter
import com.jeepchief.dh.view.myinfo.adapter.InfoRecyclerAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance(page : Int) : InfoFragment {
            val fragment = InfoFragment()
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
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
//        var adapter: InfoRecyclerAdapter? = null
        binding.apply {
            viewModel.getStatus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.run {
            status.observe(requireActivity()) {
                binding.apply {
                    val manager = LinearLayoutManager(requireContext())
                    rvInfoList.apply {
                        Log.e("status observe")
                        layoutManager = manager
                        adapter = InfoRecyclerAdapter(it.status)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
                }
            }
        }
    }
}