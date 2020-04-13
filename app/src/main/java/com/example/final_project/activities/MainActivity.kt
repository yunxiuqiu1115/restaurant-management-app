package com.example.final_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.final_project.R


import com.example.final_project.adaptor.ViewPagerAdapter
import com.example.final_project.fragments.LoginFragment
import com.example.final_project.fragments.SignUpFragment
import kotlinx.android.synthetic.main.login_page_tab_holder.*

// This is the login page
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the status bar.

        setContentView(R.layout.login_page_tab_holder)

        val adapter = ViewPagerAdapter(
            supportFragmentManager
        )

        adapter.addFragment(LoginFragment(), "LOGIN")
        adapter.addFragment(SignUpFragment(), "SIGN UP")
        viewpager_main?.adapter = adapter

        tabs_main.setupWithViewPager(viewpager_main)


    }
}
