package com.jeepchief.dh.view.myinfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.databinding.FragmentEquipBinding
import com.jeepchief.dh.model.rest.dto.AvatarDTO
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.myinfo.adapter.AvatarRecyclerAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class AvatarFragment : Fragment() {
    private var _binding: FragmentEquipBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var isAttach = false

    companion object {
        fun newInstance(page : Int) : AvatarFragment {
            val fragment = AvatarFragment()
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
        _binding = FragmentEquipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.getAvatar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAttach = true
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause from ${javaClass.simpleName}")
        viewModel.avatar.removeObserver(avatarObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.run {
            avatar.observe(requireActivity(), avatarObserver)
        }
    }

    private val avatarObserver = Observer<AvatarDTO> {
        try {
            binding.apply {
                val manager = LinearLayoutManager(requireContext())
                rvInfoList.apply {
                    layoutManager = manager
                    adapter = AvatarRecyclerAdapter(it.avatar)
                    addItemDecoration(DividerItemDecoration(
                        requireContext(), manager.orientation
                    ))
                }
            }
        } catch(e: NullPointerException) {
            e.printStackTrace()
            if(isAttach)
                Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}