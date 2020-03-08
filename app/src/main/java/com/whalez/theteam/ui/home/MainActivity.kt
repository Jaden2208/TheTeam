package com.whalez.theteam.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.whalez.theteam.R
import com.whalez.theteam.ui.sign.ErrorFragment
import com.whalez.theteam.ui.utils.finishAndGoToLoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val user = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLoginStatus()

//        btn_logout.setOnClickListener {
//            firebaseAuth.signOut()
//            finishAndGoToLoginActivity(applicationContext, this)
//            Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
//        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_main, HomeFragment()).commitAllowingStateLoss()

        nv_main.setOnNavigationItemSelectedListener(ItemSelectedListener())
    }

    private fun checkLoginStatus() {
        if(user != null){
//            text_view.text = user.email
        } else {
//            text_view.text = "로그인 되어있지 않음."
            finishAndGoToLoginActivity(applicationContext, this)
        }
    }

    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = when(item.itemId){
                R.id.home_item -> HomeFragment()
                R.id.team_item -> TeamFragment()
                R.id.user_item -> UserFragment()
                R.id.setting_item -> SettingFragment()
                else -> ErrorFragment()
            }
            transaction.replace(R.id.fl_main, fragment).commitAllowingStateLoss()
            return true
        }
    }
}


