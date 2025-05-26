package com.jeepchief.dh.features.itemsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.util.DialogHelper
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.databinding.FragmentItemSearchBinding
import com.jeepchief.dh.databinding.LayoutSearchSettingDialogBinding
import com.jeepchief.dh.features.itemsearch.adapter.SearchResultAdapter
import com.jeepchief.dh.features.main.fragment.BaseFragment
import com.jeepchief.dh.features.myinfo.ItemInfoViewModel

class ItemSearchFragment : BaseFragment() {
    private var _binding: FragmentItemSearchBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: MainViewModel by activityViewModels()
    private val itemInfoVM: ItemInfoViewModel by viewModels()

    private var wordType = NetworkConstants.WORD_TYPE_FULL
    private var minLevel = "0"
    private var maxLevel = "0"
    private var rarity = ""
    private val jsonQ = JsonObject()

    private val settingMap = mutableMapOf<String, Int>()

    private var q = ""

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
                requireActivity().run {
                    getSystemService(InputMethodManager::class.java)
                        .hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
                if(edtSearchItem.text.toString().isEmpty() || edtSearchItem.text.toString().isBlank()) {
                    Toast.makeText(requireContext(), getString(R.string.error_msg_not_allow_black), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                itemInfoVM.getSearchItems(edtSearchItem.text.toString(), wordType, q)
                Log.e("${edtSearchItem.text.toString()} / $wordType / $q")

            }
            fabBack.setOnClickListener { requireActivity().onBackPressed() }
            ivSearchSetting.setOnClickListener {
                val dlgView = LayoutSearchSettingDialogBinding.inflate(layoutInflater)
                val dlg = AlertDialog.Builder(requireContext()).create().apply {
                    setView(dlgView.root)
                    setCancelable(false)
                }

                dlgView.apply {
                    if(settingMap.isNotEmpty()) {
                        settingMap.get("wordType")?.let {
                            rgWordTypeGroup.check(it)
                        } ?: run {
                            rgRarityGroup.check(R.id.rb_word_type_front)
                        }
                        settingMap.get("rarity")?.let {
                            rgRarityGroup.check(it)
                        } ?: run {
                            rgRarityGroup.check(R.id.rb_common)
                        }

                        edtMinLevel.setText(minLevel)
                        edtMaxLevel.setText(maxLevel)
                    }
                    btnSearchSetting.setOnClickListener {
                        wordType = when(rgWordTypeGroup.checkedRadioButtonId) {
                            R.id.rb_word_type_front -> {
                                settingMap.put("wordType", R.id.rb_word_type_front)
                                NetworkConstants.WORD_TYPE_FRONT
                            }
                            R.id.rb_word_type_full -> {
                                settingMap.put("wordType", R.id.rb_word_type_full)
                                NetworkConstants.WORD_TYPE_FULL
                            }
                            R.id.rb_word_type_match -> {
                                settingMap.put("wordType", R.id.rb_word_type_match)
                                NetworkConstants.WORD_TYPE_MATCH
                            }
                            else -> NetworkConstants.WORD_TYPE_FULL
                        }

                        rarity = when(rgRarityGroup.checkedRadioButtonId) {
                            R.id.rb_uncommon -> {
                                settingMap.put("rarity", R.id.rb_uncommon)
                                rbUncommon.text.toString()
                            }
                            R.id.rb_common -> {
                                settingMap.put("rarity", R.id.rb_common)
                                rbCommon.text.toString()
                            }
                            R.id.rb_rare -> {
                                settingMap.put("rarity", R.id.rb_rare)
                                rbRare.text.toString()
                            }
                            R.id.rb_unique -> {
                                settingMap.put("rarity", R.id.rb_unique)
                                rbUnique.text.toString()
                            }
                            R.id.rb_epic -> {
                                settingMap.put("rarity", R.id.rb_epic)
                                rbEpic.text.toString()
                            }
                            R.id.rb_cron -> {
                                settingMap.put("rarity", R.id.rb_cron)
                                rbCron.text.toString()
                            }
                            R.id.rb_legendary -> {
                                settingMap.put("rarity", R.id.rb_legendary)
                                rbLegendary.text.toString()
                            }
                            R.id.rb_myth -> {
                                settingMap.put("rarity", R.id.rb_myth)
                                rbMyth.text.toString()
                            }
                            R.id.rb_all -> {
                                settingMap.put("rarity", R.id.rb_all)
                                ""
                            }
                            else -> ""
                        }
                        minLevel = edtMinLevel.text.toString()
                        maxLevel = edtMaxLevel.text.toString()

                        q = "minLevel:$minLevel,maxLevel:$maxLevel,rarity:$rarity"
                        Log.e("$q / $wordType")
                        dlg.dismiss()
                    }
                }
                dlg.show()
            }
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
        itemInfoVM.run {
            itemsSearch.observe(requireActivity()) {
                Log.e(it.rows.toString())
                if(it.rows.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.error_msg_no_result), Toast.LENGTH_SHORT).show()
                    return@observe
                }
                try {
                    binding.rvSearchItem.apply {
                        val manager = LinearLayoutManager(requireContext())
                        layoutManager = manager
                        adapter = SearchResultAdapter(it.rows!!, itemInfoVM)
                        addItemDecoration(DividerItemDecoration(
                            requireContext(), manager.orientation
                        ))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), getString(R.string.error_msg_retry), Toast.LENGTH_SHORT).show()
                }
            }
            itemInfo.observe(requireActivity()) {
                DialogHelper.itemInfoDialog(requireContext(), it).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}