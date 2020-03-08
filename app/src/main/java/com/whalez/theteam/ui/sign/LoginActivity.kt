package com.whalez.theteam.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.whalez.theteam.R
import com.whalez.theteam.ui.home.MainActivity
import com.whalez.theteam.ui.utils.ConstValues
import com.whalez.theteam.ui.utils.ConstValues.Companion.TAG
import com.whalez.theteam.ui.utils.hideLoading
import com.whalez.theteam.ui.utils.showLoading
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            if (btn_login.text == "로그인") {
                performLogin()
            } else {
                et_email.visibility = View.VISIBLE
                et_password.visibility = View.VISIBLE
                btn_login.text = "로그인"
            }
        }

        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {

        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if (email.isEmpty() || password.length < 6) {
            val message = "아이디 또는 비밀번호를 확인해주세요."
            showDialogAndClearPassword(message)
        } else {

            showLoading(loading_layout)

            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if(!user!!.isEmailVerified) {

                            hideLoading(loading_layout)

                            val message = "아직 이메일을 통한 본인 인증을 완료하지 않았습니다. 이메일을 확인해주세요."
                            showDialogAndClearPassword(message)
                        } else {
//                            userSessionManager.createSession(userId)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Log.d(TAG, task.toString())
                    }
                }
                .addOnFailureListener {

                    hideLoading(loading_layout)

                    val errorMessage = when (it.message) {
                        getString(R.string.email_format_error) ->
                            "이메일 형식이 올바르지 않습니다."
                        getString(R.string.network_error) ->
                            "네트워크 연결상태가 올바르지 않습니다."
                        getString(R.string.password_error), getString(R.string.not_exist_user) ->
                            "이메일 또는 비밀번호가 일치하지 않습니다."
                        else ->
                            "알 수 없는 오류가 발생했습니다! 관리자에게 문의주세요. ${it.message}"
                    }
                    AlertDialog.Builder(
                            ContextThemeWrapper(
                                this@LoginActivity,
                                R.style.MyAlertDialogStyle
                            )
                        ).setMessage(errorMessage)
                        .setPositiveButton("확인") { _, _ ->
                            et_email.text.clear()
                            et_password.text.clear()
                        }
                        .show()
                }
        }
    }

    private fun showDialogAndClearPassword(message: String){
        AlertDialog.Builder(ContextThemeWrapper(this@LoginActivity, R.style.MyAlertDialogStyle))
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
                et_password.text.clear()
            }.show()
    }


}
