package com.jeepchief.dh.view.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentChangeCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSearchCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSelectCharacterBinding
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.character.adapter.ChangeCharacterAdapter
import com.jeepchief.dh.view.main.adapter.SelectCharacterAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class ChangeCharacterFragment : Fragment() {
    private var _binding: FragmentChangeCharacterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var isObserved = false
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
        viewModel.getCharacterList(requireContext())

//        CoroutineScope(Dispatchers.IO).launch {
//            val list = DhDatabase.getInstance(requireContext()).getCharactersDAO()
//                .getCharacters()
//            withContext(Dispatchers.Main) {
//                binding.rvCharacterGrid.apply {
//                    val manager = LinearLayoutManager(requireContext())
//                    layoutManager = manager
//                    adapter = ChangeCharacterAdapter(list)
//                    addItemDecoration(DividerItemDecoration(
//                        requireContext(), manager.orientation
//                    ))
//                }
//            }
//        }
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
                        viewModel.getCharacters(edtInsertId.text.toString())
                        dlg.dismiss()
                    }
                }

                dlg.show()
            }
        }
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
                    adapter = ChangeCharacterAdapter(it)
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                    isObserved = true
                }
            }

            characters.observe(requireActivity()) { dto ->
                val dlgView = LayoutDialogSelectCharacterBinding.inflate(LayoutInflater.from(requireActivity()))
                val dlg = AlertDialog.Builder(requireContext()).create().apply {
                    setView(dlgView.root)
                    setCancelable(false)
                }
                dlgView.apply {
                    rvCharacterList.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = SelectCharacterAdapter(dto.characterRows, dlg)
                    }
                }
                dlg.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}