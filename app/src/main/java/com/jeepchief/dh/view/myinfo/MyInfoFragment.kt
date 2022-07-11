package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jeepchief.dh.databinding.FragmentMyInfoBinding
import com.jeepchief.dh.view.myinfo.adapter.MyInfoViewpagerAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class MyInfoFragment : Fragment() {
    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    companion object val TAB_TITLE = listOf("스탯", "장착 아이템", "장착 아바타", "크리쳐/휘장")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fabBack.setOnClickListener { requireActivity().onBackPressed() }

            vpMyInfo.adapter = MyInfoViewpagerAdapter(requireActivity(), 4)
            TabLayoutMediator(tlInfoTab, vpMyInfo) { tab: TabLayout.Tab, i: Int ->
                tab.text = TAB_TITLE[i]
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}