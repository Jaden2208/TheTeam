package com.whalez.theteam.ui.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.whalez.theteam.R
import com.whalez.theteam.ui.sign.RegisterPagerAdapter.Companion.pageCounts
import kotlinx.android.synthetic.main.activity_register.*

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

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    when (position) {
                        0 -> btn_back.visibility = View.GONE
                        else -> btn_back.visibility = View.VISIBLE
                    }
                    when (position) {
                        pageCounts - 1 -> btn_next.text = "저장"
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
        }
    }
}
