package com.jeepchief.dh.view.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.FragmentMainBinding
import com.jeepchief.dh.util.Log
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
//                navController.navigate(R.id.action_mainFragment_to_auctionFragment)
                Toast.makeText(requireContext(), getString(R.string.alert_msg_not_impl_yet), Toast.LENGTH_SHORT).show()
            }
            btnGoChangeCharacter.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_changeCharacterFragment)
            }
            btnGoDictionary.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_dictionaryFragment)
            }

            ivBackground.setImageResource(randomBgImage())

//            requireActivity().run {
//                window.apply {
//                    setFlags(
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    )
//                }
//                if(Build.VERSION.SDK_INT >= 30) {
//                    WindowCompat.setDecorFitsSystemWindows(window, false)
//                }
//            }
//
//            llMainFragment.setPadding(
//                0,
//                statusBarHeight(),
//                0,
//                navigationHeight()
//            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun randomBgImage() : Int = when(Random.nextInt(4)) {
        0 -> R.drawable.main_background
        1 -> R.drawable.main_background_2
        2 -> R.drawable.main_background_3
        else -> R.drawable.main_backgroujnd_4
    }

    private fun statusBarHeight() : Int {
        var size = 0
        requireContext().run {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

//            return if(resourceId > 0) resources.getDimensionPixelSize(resourceId)
//            else 0
            size = resources.getDimensionPixelSize(resourceId)
        }
        Log.e("statusBar height is $size")
        return size
    }

    private fun navigationHeight() : Int {
        var size = 0
        requireContext().run {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

//            return if(resourceId > 0) resources.getDimensionPixelSize(resourceId)
//            else 0
            size = resources.getDimensionPixelSize(resourceId)
        }
        Log.e("navigation height is $size")
        return size
    }
}