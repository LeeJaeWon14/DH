package com.jeepchief.dh.features.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.databinding.FragmentMyInfoBinding
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.fragment.BaseFragment
import com.jeepchief.dh.features.myinfo.adapter.MyInfoViewpagerAdapter

class MyInfoFragment : BaseFragment() {
    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    companion object val TAB_TITLE = listOf("스탯", "장착 아이템", "장착 아바타", "크리쳐/휘장", "탈리스만", "버프스킬 장비"/*, "버프스킬 아바타"*/)
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

            vpMyInfo.adapter = MyInfoViewpagerAdapter(requireActivity(), TAB_TITLE.size)
            TabLayoutMediator(tlInfoTab, vpMyInfo) { tab: TabLayout.Tab, i: Int ->
                tab.text = TAB_TITLE[i]
            }.attach()

            viewModel.nowCharacterInfo.value?.let {
                Log.e("server > ${it.serverId} / characterId > ${it.characterId}")
                Glide.with(requireContext())
                    .load(String.format(NetworkConstants.CHARACTER_URL, it.serverId, it.characterId, 0))
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
}