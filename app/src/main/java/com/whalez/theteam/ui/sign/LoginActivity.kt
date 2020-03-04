package com.whalez.theteam.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.whalez.theteam.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            if(btn_login.text == "로그인"){
                Toast.makeText(this, "로그인 시도!", Toast.LENGTH_SHORT).show()
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
}
