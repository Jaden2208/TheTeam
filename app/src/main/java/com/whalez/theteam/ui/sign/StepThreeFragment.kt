package com.whalez.theteam.ui.sign

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whalez.theteam.R
import com.whalez.theteam.ui.utils.ConstValues.Companion.TAG
import kotlinx.android.synthetic.main.fragment_step_three.*

class StepThreeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_three, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated3")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume3")
        tv_name.text = RegisterPagerAdapter.name
        tv_email.text = RegisterPagerAdapter.email
        tv_age.text = RegisterPagerAdapter.age
        tv_area.text = RegisterPagerAdapter.area
        tv_introduce.text = RegisterPagerAdapter.introduce
    }

}
