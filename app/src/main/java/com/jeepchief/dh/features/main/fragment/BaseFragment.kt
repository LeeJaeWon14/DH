package com.jeepchief.dh.features.main.fragment

import android.os.Bundle
import android.view.View

open class BaseFragment : ContextFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (requireContext() as MainActivity).updateActionbar()

    }

    override fun onDestroy() {
        super.onDestroy()
//        (requireContext() as MainActivity).updateActionbar()
    }
}