package com.example.final_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.final_project.R
import com.example.final_project.adaptor.ViewPagerAdapter
import com.example.final_project.adaptor.ViewPagerAdapter2
import com.example.final_project.fragments.HomeFragment
import com.example.final_project.fragments.LoginFragment
import com.example.final_project.fragments.SignUpFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.login_page_tab_holder.*

class HomeActivity : AppCompatActivity() {
    private lateinit var vpa : ViewPagerAdapter2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        menu_bottom.setItemSelected(0)
//        menu_bottom.setItemEnabled(0,true)
        menu_bottom.setOnItemSelectedListener{
            when(it){
                R.id.home -> viewpager.currentItem = 0
                R.id.order -> viewpager.currentItem = 1
                R.id.service -> viewpager.currentItem = 2
                R.id.accessory -> viewpager.currentItem = 3
            }
        }


        vpa = ViewPagerAdapter2(supportFragmentManager)
        viewpager.adapter = vpa
        viewpager.offscreenPageLimit = 3



    }
}
