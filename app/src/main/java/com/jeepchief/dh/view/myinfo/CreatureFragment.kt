package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jeepchief.dh.databinding.FragmentCreatureBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.myinfo.adapter.CreatureAdapter
import com.jeepchief.dh.view.myinfo.adapter.FlagAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class CreatureFragment : Fragment() {
    private var _binding: FragmentCreatureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance(page : Int) : CreatureFragment {
            val fragment = CreatureFragment()
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
        _binding = FragmentCreatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.run {
            getCreature()
            getFlag()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            creature.observe(requireActivity()) {
                binding.apply {
                    // Init creature info
                    Glide.with(requireContext())
                        .load(String.format(NetworkConstants.ITEM_URL, it.creature.itemId))
                        .override(112, 112)
                        .centerCrop()
                        .into(ivCreature)

                    tvCreature.apply {
                        text = it.creature.itemName
                        setTextColor(RarityChecker.convertColor(it.creature.itemRarity))
                    }
                    rvCreature.apply {
                        val manager = LinearLayoutManager(requireContext())
                        layoutManager = manager
                        adapter = CreatureAdapter(it.creature.artifact)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
                }
            }
            flag.observe(requireActivity()) {
                binding.apply {
                    // Init flag info
                    it.flag?.let { // When not equipped flag.
                        Glide.with(requireContext())
                            .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                            .override(112, 112)
                            .centerCrop()
                            .into(ivFlag)

                        tvFlag.run {
                            text = it.itemName.plus("(Lv. ${it.itemAvailableLevel})")
                            setTextColor(RarityChecker.convertColor(it.itemRarity))
                        }
                        tvFlagAbility.text = it.itemAbility
                        Log.e(it.gems.toString())
                        rvFlag.apply {
                            val manager = LinearLayoutManager(requireContext())
                            layoutManager = manager
                            adapter = FlagAdapter(it.gems)
                            addItemDecoration(DividerItemDecoration(
                                requireContext(), manager.orientation
                            ))
                        }
                    } ?: run {
                        tvFlag.text = "장착된 휘장이 없습니다."
                        tvFlagAbility.isVisible = false
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}