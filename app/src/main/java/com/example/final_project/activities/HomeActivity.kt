package com.example.final_project.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.final_project.R
import com.example.final_project.adaptor.ViewPagerAdapter2
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private lateinit var vpa : ViewPagerAdapter2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        menu_bottom.setOnItemSelectedListener{
            when(it){
                R.id.home -> viewpager.currentItem = 0
                R.id.menu -> viewpager.currentItem = 1
                R.id.profile -> viewpager.currentItem = 2
                R.id.accessory -> viewpager.currentItem = 3
            }
        }



        vpa = ViewPagerAdapter2(supportFragmentManager)
        viewpager.adapter = vpa
        viewpager.offscreenPageLimit = 3
        viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            // This method will be invoked when a new page becomes selected.
            override fun onPageSelected(position: Int) {

                when(position){
                    0 -> menu_bottom.setItemSelected(R.id.home)
                    1 -> menu_bottom.setItemSelected(R.id.menu)
                    2 -> menu_bottom.setItemSelected(R.id.profile)
                    3 -> menu_bottom.setItemSelected(R.id.accessory)
                }
            }

            // This method will be invoked when the current page is scrolled
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            override fun onPageScrollStateChanged(state: Int) {
                // Code goes here
            }
        })
    }
}
