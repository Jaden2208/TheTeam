package com.whalez.theteam.ui.sign

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.whalez.theteam.R
import com.whalez.theteam.ui.utils.ConstValues
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_step_one.*

class StepOneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_one, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(ConstValues.TAG, "onActivityCreated1")
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        et_password1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswords()
            }
        })

        et_password2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswords()
            }
        })

    }


    private fun checkPasswords() {
        val password1 = et_password1.text.toString()
        val password2 = et_password2.text.toString()
        if (password1 == "" || password2 == "") {
            password_check_message.visibility = View.INVISIBLE
        } else if (password1 == password2) {
            password_check_message.visibility = View.VISIBLE
            if (password1.length < 6) {
                password_check_message.text = "비밀번호는 6자 이상이어야 합니다."
                password_check_message.setTextColor(
                    ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.colorAccent
                    )
                )
            } else {
                password_check_message.text = "비밀번호가 일치합니다."
                password_check_message.setTextColor(
                    ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.colorPrimaryDark
                    )
                )
            }
        } else {
            password_check_message.visibility = View.VISIBLE
            password_check_message.text = "비밀번호가 일치하지 않습니다."
            password_check_message.setTextColor(
                ContextCompat.getColor(
                    activity!!.applicationContext,
                    R.color.colorAccent
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        RegisterPagerAdapter.name = et_name.text.trim().toString()
        RegisterPagerAdapter.email = et_email.text.trim().toString()
    }
}
