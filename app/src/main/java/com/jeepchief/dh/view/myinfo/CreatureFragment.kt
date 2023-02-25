package com.jeepchief.dh.view.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentCreatureBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.dto.CreatureDTO
import com.jeepchief.dh.model.rest.dto.FlagDTO
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.DialogHelper
import com.jeepchief.dh.util.RarityChecker
import com.jeepchief.dh.view.main.fragment.ContextFragment
import com.jeepchief.dh.view.myinfo.adapter.CreatureAdapter
import com.jeepchief.dh.view.myinfo.adapter.FlagAdapter
import com.jeepchief.dh.viewmodel.ItemInfoViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class CreatureFragment : ContextFragment() {
    private var _binding: FragmentCreatureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemVM: ItemInfoViewModel by viewModels()

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

    override fun onPause() {
        super.onPause()
        viewModel.run {
            creature.removeObserver(creatureObserver)
            flag.removeObserver(flagObserver)
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            creature.observe(requireActivity(), creatureObserver)
            flag.observe(requireActivity(), flagObserver)
        }

        itemVM.itemInfo.observe(requireActivity(), itemInfoObserver)
    }

    private var itemInfoObserver = Observer<ItemsDTO> {
        DialogHelper.itemInfoDialog(mContext, it).show()
    }

    private var creatureObserver = Observer<CreatureDTO> {
        try {
            binding.apply {
                // Init creature info
                it.creature?.let {
                    Glide.with(mContext)
                        .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                        .override(112, 112)
                        .centerCrop()
                        .into(ivCreature)

                    tvCreature.apply {
                        text = it.itemName
                        setTextColor(RarityChecker.convertColor(it.itemRarity))
                    }
                    rvCreature.apply {
                        val manager = LinearLayoutManager(mContext)
                        layoutManager = manager
                        adapter = CreatureAdapter(it.artifact)
                        addItemDecoration(DividerItemDecoration(
                            mContext, manager.orientation
                        ))
                    }
                    llCreature.setOnClickListener { _ ->
                        itemVM.getItemInfo(it.itemId)
                    }
                } ?: run {
                    tvCreature.run {
                        text = "장착된 크리쳐가 없습니다."
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(mContext, getString(R.string.error_msg_no_creature), Toast.LENGTH_SHORT).show()
        }
    }

    private var flagObserver = Observer<FlagDTO> {
        try {
            binding.apply {
                // Init flag info
                it.flag?.let { // When not equipped flag.
                    Glide.with(mContext)
                        .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
                        .override(112, 112)
                        .centerCrop()
                        .into(ivFlag)

                    tvFlag.run {
                        text = it.itemName.plus("(Lv. ${it.itemAvailableLevel})")
                        setTextColor(RarityChecker.convertColor(it.itemRarity))
                    }
                    tvFlagAbility.text = it.itemAbility
                    rvFlag.apply {
                        val manager = LinearLayoutManager(mContext)
                        layoutManager = manager
                        adapter = FlagAdapter(it.gems)
                        addItemDecoration(DividerItemDecoration(
                            mContext, manager.orientation
                        ))
                    }
                    llFlag.setOnClickListener { _ ->
                        itemVM.getItemInfo(it.itemId)
                    }
                } ?: run {
                    tvFlag.run {
                        text = "장착된 휘장이 없습니다."
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    tvFlagAbility.isVisible = false
                }
            }
        } catch (e: Exception) {
            Toast.makeText(mContext, getString(R.string.error_msg_no_flag), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}