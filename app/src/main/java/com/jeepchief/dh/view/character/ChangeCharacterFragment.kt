package com.jeepchief.dh.view.character

import android.content.Context
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
import com.jeepchief.dh.databinding.FragmentChangeCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSearchCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSelectCharacterBinding
import com.jeepchief.dh.model.database.characters.CharactersEntity
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.character.adapter.ChangeCharacterAdapter
import com.jeepchief.dh.view.main.adapter.SelectCharacterAdapter
import com.jeepchief.dh.view.main.fragment.SuperFragment
import com.jeepchief.dh.viewmodel.CharacterViewModel
import com.jeepchief.dh.viewmodel.MainViewModel

class ChangeCharacterFragment : SuperFragment() {
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
                            viewModel.getCharacters(edtInsertId.text.toString())
                            dlg.dismiss()
                        }
                    }
                    btnCancel.setOnClickListener { dlg.dismiss() }
                }

                dlg.show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAttached = true
    }

    private fun observeViewModel() {
        viewModel.run {
            characterList.observe(requireActivity()) {
                if(isObserved) return@observe
                if(it.isNullOrEmpty()) {
                    Log.e("CharacterEntity is null!")
                    return@observe
                }
                binding.rvCharacterGrid.apply {
                    val manager = LinearLayoutManager(requireContext())
                    layoutManager = manager
                    adapter = ChangeCharacterAdapter(it, viewModel.servers.value!!)
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                    isObserved = true
                }
            }
        }

        characterVM.characters.observe(requireActivity()) { dto ->
            if(!isAttached) return@observe
            Log.e("characters observed")
            val dlgView = LayoutDialogSelectCharacterBinding.inflate(LayoutInflater.from(requireActivity()))
            val dlg = AlertDialog.Builder(requireContext()).create().apply {
                setView(dlgView.root)
                setCancelable(false)
            }
            dlgView.apply {
                rvCharacterList.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = SelectCharacterAdapter(dto.characterRows, dlg, viewModel.servers.value!!)
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

    fun updateList(list: List<CharactersEntity>) {
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