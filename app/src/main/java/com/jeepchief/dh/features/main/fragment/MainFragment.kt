package com.jeepchief.dh.features.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentMainBinding
import kotlin.random.Random

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
//                Toast.makeText(requireContext(), getString(R.string.alert_msg_not_impl_yet), Toast.LENGTH_SHORT).show()
            }
            btnGoChangeCharacter.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_changeCharacterFragment)
            }
            btnGoDictionary.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_dictionaryFragment)
            }
            btnGoTimeline.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_timelineFragment)
            }

            ivBackground.setImageResource(randomBgImage())
            llMainFragment.setOnTouchListener { thisLayout, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        thisLayout.isVisible = false
                    }
                    MotionEvent.ACTION_UP -> {
                        thisLayout.isVisible = true
                    }
                }
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun randomBgImage() : Int = when(Random.nextInt(6)) {
        0 -> R.drawable.main_background
        1 -> R.drawable.main_background_2
        2 -> R.drawable.main_background_3
        4 -> R.drawable.main_backgroujnd_4
        5 -> R.drawable.main_background_5
        else -> R.drawable.main_background_6
    }
}