package com.jeepchief.dh.view.auction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeepchief.dh.databinding.FragmentAuctionBinding
import com.jeepchief.dh.view.main.fragment.BaseFragment

class AuctionFragment : BaseFragment() {
    private var _binding: FragmentAuctionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuctionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fabBack.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}