package com.jeepchief.dh.view.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Toast.makeText(requireContext(), "MainFragment", Toast.LENGTH_SHORT).show()
        navController = Navigation.findNavController(view)

        binding.apply {
            btnGoMyInfo.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_myInfoFragment)
            }
            btnGoSearchItems.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_itemSearchFragment)
            }
            btnGoAuction.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_auctionFragment)
            }
            btnGoChangeCharacter.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_changeCharacterFragment)
            }
            btnGoDictionary.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_dictionaryFragment)
            }

//            Glide.with(requireContext())
//                .load("https://df.nexon.com/df/pg/dfonwallpaper?mode=view&idx=220")
//                .centerCrop()
//                .into(ivBackground)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}