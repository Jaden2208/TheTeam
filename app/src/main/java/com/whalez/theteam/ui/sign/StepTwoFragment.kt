package com.whalez.theteam.ui.sign

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whalez.theteam.R
import com.whalez.theteam.utils.ConstValues
import kotlinx.android.synthetic.main.fragment_register_step_two.*

class StepTwoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_step_two, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(ConstValues.TAG, "onActivityCreated2")
    }

    override fun onPause() {
        super.onPause()
        Log.d(ConstValues.TAG, "onPause2")
        RegisterPagerAdapter.age = et_age.text.trim().toString()
        RegisterPagerAdapter.area = et_area.text.trim().toString()
        RegisterPagerAdapter.introduce = et_introduce.text.trim().toString()
    }
}
