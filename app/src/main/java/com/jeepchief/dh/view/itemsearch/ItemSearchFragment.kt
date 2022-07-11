package com.jeepchief.dh.view.itemsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentItemSearchBinding
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
        viewModel.itemsSearch.observe(requireActivity()) {
            if(it.rows.isEmpty()) {
                Toast.makeText(requireContext(), "결과가 없습니다.", Toast.LENGTH_SHORT).show()
                return@observe
            }
            binding.rvSearchItem.apply {
                val manager = LinearLayoutManager(requireContext())
                layoutManager = manager
                adapter = SearchResultAdapter(it.rows)
                addItemDecoration(DividerItemDecoration(
                    requireContext(), manager.orientation
                ))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}