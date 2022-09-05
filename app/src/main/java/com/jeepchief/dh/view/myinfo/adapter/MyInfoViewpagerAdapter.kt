package com.jeepchief.dh.view.myinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.myinfo.*

class MyInfoViewpagerAdapter(fragmentActivity: FragmentActivity, private val count: Int) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> InfoFragment.newInstance(0)
            1 -> EquipmentFragment.newInstance(0)
            2 -> AvatarFragment.newInstance(2)
            3 -> CreatureFragment.newInstance(0)
            4 -> TalismanFragment()
            else -> {
                Log.e("Cannot find fragment")
                Fragment()
            }
        }
    }
}