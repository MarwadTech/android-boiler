package com.marwadtech.userapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CustomPagerAdapter(fragmentManager: FragmentManager, private val tabFragmentList: ArrayList<Fragment>) :
    FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return tabFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return tabFragmentList[position]
    }
}
