package com.whalez.theteam.ui.sign

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.whalez.theteam.R
import com.whalez.theteam.R.string.*
import com.whalez.theteam.utils.ConstValues.Companion.TAG
import kotlinx.android.synthetic.main.fragment_register_step_one.*

class StepOneFragment : Fragment() {

//    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_step_one, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated1")
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        profile_image.setOnClickListener {
            Log.d(TAG, "사진 클릭")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult1")

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            RegisterPagerAdapter.photoUri = data.data
            Glide.with(this)
                .load(RegisterPagerAdapter.photoUri)
                .into(profile_image)
        }
    }


    private fun checkPasswords() {
        val password1 = et_password1.text.toString()
        val password2 = et_password2.text.toString()
        if (password1 == "" || password2 == "") {
            password_check_message.visibility = View.INVISIBLE
        } else if (password1 == password2) {
            password_check_message.visibility = View.VISIBLE
            if (password1.length < 6) {
                password_check_message.text = getString(password_length_short)
                password_check_message.setTextColor(
                    ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.colorAccent
                    )
                )
            } else {
                password_check_message.text = getString(password_ok)
                password_check_message.setTextColor(
                    ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.colorPrimaryDark
                    )
                )
            }
        } else {
            password_check_message.visibility = View.VISIBLE
            password_check_message.text = getString(password_not_ok)
            password_check_message.setTextColor(
                ContextCompat.getColor(
                    activity!!.applicationContext,
                    R.color.colorAccent
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val photoUri = RegisterPagerAdapter.photoUri
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .into(profile_image)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause1")
        val name = et_name.text.trim().toString()
        val email = et_email.text.trim().toString()
        if (name.isNotEmpty() && email.isNotEmpty() &&
            password_check_message.text.toString() == getString(password_ok)
        ) {
            RegisterPagerAdapter.readyToRegister = true
            RegisterPagerAdapter.password = et_password1.text.toString()
        }
        RegisterPagerAdapter.name = name
        RegisterPagerAdapter.email = email
    }
}
