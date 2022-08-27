package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentMyInfoBinding
import com.jeepchief.dh.model.NetworkConstants
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

//        observeViewModel()
//        viewModel.getEquipment()

        binding.apply {
            fabBack.setOnClickListener { requireActivity().onBackPressed() }

            vpMyInfo.adapter = MyInfoViewpagerAdapter(requireActivity(), 4)
            TabLayoutMediator(tlInfoTab, vpMyInfo) { tab: TabLayout.Tab, i: Int ->
                tab.text = TAB_TITLE[i]
            }.attach()

            viewModel.mySimpleInfo.value?.let {
                Glide.with(requireContext())
                    .load(String.format(NetworkConstants.CHARACTER_URL, it.serverId, it.characterId))
                    .error(R.drawable.ic_launcher_foreground)
                    .thumbnail(0.2f)
                    .override(400, 460)
                    .into(ivCharacterView)
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun observeViewModel() {
//        viewModel.run {
//            equipment.observe(requireActivity()) { dto ->
//                binding.rvEquipment.apply {
//                    layoutManager = GridLayoutManager(requireContext(), 3)
//                    adapter = EquipViewAdapter(dto.equipment)
//                }
//            }
//        }
//    }
}