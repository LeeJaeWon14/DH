package com.jeepchief.dh.view.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentChangeCharacterBinding
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.character.adapter.ChangeCharacterAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class ChangeCharacterFragment : Fragment() {
    private var _binding: FragmentChangeCharacterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
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
        // init Ui
        binding.apply {

            fabBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.characterList.observe(requireActivity()) {
            if(it.isNullOrEmpty()) {
                Log.e("CharacterEntity is null!")
                return@observe
            }
            Log.e("observe character list, $it")
            binding.rvCharacterGrid.apply {
//                val manager = GridLayoutManager(requireContext(), 3)
                val manager = LinearLayoutManager(requireContext())
                layoutManager = manager
                adapter = ChangeCharacterAdapter(it)
                addItemDecoration(DividerItemDecoration(
                    requireContext(), manager.orientation
                ))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}