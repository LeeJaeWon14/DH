package com.jeepchief.dh.view.main.fragment

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * @author LeeJW
 * Context 초기화를 공통으로 하기위한 Super Class
 */
open class ContextFragment : Fragment() {
    lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}