package com.whalez.theteam.ui.sign

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whalez.theteam.R
import com.whalez.theteam.R.string.email_already_registered
import com.whalez.theteam.R.string.email_format_error
import com.whalez.theteam.data.User
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.age
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.area
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.introduce
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.name
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.pageCounts
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.photoUri
import com.whalez.theteam.ui.utils.ConstValues
import com.whalez.theteam.ui.utils.ConstValues.Companion.TAG
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_step_one.*
import java.util.*

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
            if(nextPage < pageCounts)
                vp_register.setCurrentItem(nextPage, true)
            else { // 마지막 페이지인 경우
                if(RegisterPagerAdapter.readyToRegister) {
                    performRegister()
                } else {
                    Toast.makeText(this, "기본 정보 페이지(1/3)를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if(photoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "사진 저장 완료 : ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {url ->
                    saveUserToFirebaseDatabase(url.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "사진 저장 실패 : ${it.message}")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        Log.d(TAG, "DB저장 시작")
        val uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, name, age, area, introduce, profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "firebase DB에 저장됨!")
            }
    }

    private fun performRegister(){
        val userEmail = RegisterPagerAdapter.email
        val userPassword = RegisterPagerAdapter.password
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    uploadImageToFirebaseStorage()
                    Toast.makeText(this, "회원가입 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    photoUri = null
                    finish()
                }
            }
            .addOnFailureListener {
                var message = ""
                message = when (it.message) {
                    getString(email_format_error) -> "입력한 이메일을 다시 확인해주세요."
                    getString(email_already_registered) -> "이미 가입된 이메일입니다."
                    else -> it.message.toString()
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
    }
}
