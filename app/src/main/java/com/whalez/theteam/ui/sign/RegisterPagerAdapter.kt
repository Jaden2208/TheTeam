package com.whalez.theteam.ui.sign

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class RegisterPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val pageCounts = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> StepOneFragment()

            1 -> StepTwoFragment()

            2 -> StepThreeFragment()

            else -> ErrorFragment()
        }
    }

    override fun getCount(): Int {
        return pageCounts
    }

}
