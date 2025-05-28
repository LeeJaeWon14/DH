package com.jeepchief.dh.features.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.ProgressDialog
import com.jeepchief.dh.databinding.FragmentChangeCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSearchCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSelectCharacterBinding
import com.jeepchief.dh.features.character.adapter.ChangeCharacterAdapter
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.adapter.SelectCharacterAdapter
import com.jeepchief.dh.features.main.fragment.BaseFragment

class ChangeCharacterFragment : BaseFragment() {
    private var _binding: FragmentChangeCharacterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val characterVM: CharacterViewModel by viewModels()
    private var isObserved = false
//    private var isCharacterObserved = false
    private var isAttached = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
//        isCharacterObserved = true
        ProgressDialog.showProgressDialog(requireContext()).show()
        viewModel.getCharacterList(requireContext())

        // init Ui
        binding.apply {

            fabBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            fabAddCharacter.setOnClickListener {
                val dlgView = LayoutDialogSearchCharacterBinding.inflate(layoutInflater)
                val dlg = AlertDialog.Builder(requireContext()).create().apply {
                    setView(dlgView.root)
                    setCancelable(false)
                }

                dlgView.apply {
                    btnInsertOk.setOnClickListener {
                        if(edtInsertId.text.toString().isEmpty())
                            Toast.makeText(requireContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                        else {
                            characterVM.getCharacters(name = edtInsertId.text.toString())
                            dlg.dismiss()
                        }
                    }
                    btnCancel.setOnClickListener { dlg.dismiss() }
                }

                dlg.show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.run {
//            characterList.observe(requireActivity()) {
//                if(isObserved) return@observe
//                if(it.isNullOrEmpty()) {
//                    Log.e("CharacterEntity is null!")
//                    return@observe
//                }
//
//                CoroutineScope(Dispatchers.Main).launch {
//                    val dtoList = mutableListOf<CharacterRows>()
//                    withContext(Dispatchers.IO) {
//                        it.forEach { entity ->
////                            val dto = characterVM.dfService.getCharacters(entity.serverId, entity.characterName)
////                            Log.e("$dto")
////                            dtoList.add(dto.characterRows[0])
//                        }
//                    }
//
//                    ProgressDialog.dismissDialog()
//                    binding.rvCharacterGrid.apply {
//                        val manager = LinearLayoutManager(requireContext())
//                        layoutManager = manager
//                        adapter = ChangeCharacterAdapter(dtoList, viewModel.servers.value!!)
//                        addItemDecoration(DividerItemDecoration(
//                            requireContext(), manager.orientation
//                        ))
//                        isObserved = true
//                    }
//                }
//            }
        }

        characterVM.characters.observe(requireActivity()) { dto ->
            Log.e("come in, $dto")
            if(dto.characterRows.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No Search Data", Toast.LENGTH_SHORT).show()
                return@observe
            }
            val dlgView = LayoutDialogSelectCharacterBinding.inflate(LayoutInflater.from(requireActivity()))
            val dlg = AlertDialog.Builder(requireContext()).create().apply {
                setView(dlgView.root)
                setCancelable(false)
            }
            dlgView.apply {
                rvCharacterList.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = SelectCharacterAdapter(dto.characterRows, viewModel.servers.value!!) { dlg.dismiss() }
                }
                btnCancel.setOnClickListener { dlg.dismiss() }
            }
            dlg.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateList(list: List<CharacterRows>) {
        binding.rvCharacterGrid.apply {
            val manager = LinearLayoutManager(requireContext())
            layoutManager = manager
            adapter = ChangeCharacterAdapter(list, viewModel.servers.value!!)
            addItemDecoration(DividerItemDecoration(
                requireContext(), manager.orientation
            ))
        }
    }
}