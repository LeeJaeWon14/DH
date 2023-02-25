package com.jeepchief.dh.view.main.fragment

import android.os.Bundle
import android.view.View
import com.jeepchief.dh.view.main.activity.MainActivity

open class BaseFragment : ContextFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as MainActivity).updateActionbar()

    }

    override fun onDestroy() {
        super.onDestroy()
        (requireContext() as MainActivity).updateActionbar()
    }
}