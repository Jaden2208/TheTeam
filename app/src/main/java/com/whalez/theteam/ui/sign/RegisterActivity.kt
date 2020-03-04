package com.whalez.theteam.ui.sign

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.whalez.theteam.R
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.pageCounts
import com.whalez.theteam.ui.utils.ConstValues
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_step_one.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val registerPagerAdapter = RegisterPagerAdapter(supportFragmentManager)
        vp_register.apply {
            adapter = registerPagerAdapter
            addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                @SuppressLint("SetTextI18n")
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    tv_page.text = "${position+1}/$pageCounts"
                    when (position) {
                        0 -> btn_back.visibility = View.GONE
                        else -> btn_back.visibility = View.VISIBLE
                    }
                    when (position) {
                        pageCounts - 1 -> {
                            btn_next.text = "가입하기"
                        }
                        else -> btn_next.text = "다음"
                    }
                }

                override fun onPageSelected(position: Int) {
                }

            })
        }


        btn_back.setOnClickListener {
            val backPage = vp_register.currentItem - 1
            if(backPage >= 0)
                vp_register.setCurrentItem(backPage, true)
        }
        btn_next.setOnClickListener {
            val nextPage = vp_register.currentItem + 1
            if(nextPage <= pageCounts)
                vp_register.setCurrentItem(nextPage, true)
            else {
                registerUser()
            }
        }
    }

    private fun registerUser(){
        val userEmail = et_email.text.trim().toString()
        val userPassword = et_password1.text.toString()
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.d(ConstValues.TAG, "$user 회원가입 되었습니다.")
                } else {
                    Log.d(ConstValues.TAG, it.toString())
                }
            }
    }
}
