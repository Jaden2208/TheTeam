package com.whalez.theteam.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.whalez.theteam.ui.sign.LoginActivity
import com.whalez.theteam.utils.ConstValues.Companion.TAG

fun showLoading(frameLayout: FrameLayout) {
    Log.d(TAG, "loading start")
    frameLayout.visibility = View.VISIBLE
}

fun hideLoading(frameLayout: FrameLayout) {
    Log.d(TAG, "loading finish")
    frameLayout.visibility = View.GONE
}

fun finishAndGoToLoginActivity(context: Context, activity: Activity){
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    activity.finish()
}