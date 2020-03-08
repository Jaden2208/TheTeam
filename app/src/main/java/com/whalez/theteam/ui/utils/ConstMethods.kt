package com.whalez.theteam.ui.utils

import android.view.View
import android.widget.FrameLayout

fun showLoading(frameLayout: FrameLayout) {
    frameLayout.visibility = View.VISIBLE
}

fun hideLoading(frameLayout: FrameLayout) {
    frameLayout.visibility = View.GONE
}