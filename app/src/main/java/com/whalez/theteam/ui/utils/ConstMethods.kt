package com.whalez.theteam.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.startActivity
import com.whalez.theteam.ui.home.MainActivity
import com.whalez.theteam.ui.sign.LoginActivity

fun showLoading(frameLayout: FrameLayout) {
    frameLayout.visibility = View.VISIBLE
}

fun hideLoading(frameLayout: FrameLayout) {
    frameLayout.visibility = View.GONE
}

fun finishAndGoToLoginActivity(context: Context, activity: Activity){
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    activity.finish()
}