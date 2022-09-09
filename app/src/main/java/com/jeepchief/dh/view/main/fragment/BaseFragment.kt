package com.jeepchief.dh.view.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jeepchief.dh.view.main.activity.MainActivity

open class BaseFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as MainActivity).updateActionbar()


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

    override fun onDestroy() {
        super.onDestroy()
        (requireContext() as MainActivity).updateActionbar()
    }
}