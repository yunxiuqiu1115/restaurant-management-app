package com.example.final_project.activities


import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.final_project.R
import com.example.final_project.adaptor.ViewPagerAdapter
import com.example.final_project.fragments.LoginFragment
import com.example.final_project.fragments.SignUpFragment
import com.example.final_project.util.Restaurant
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.login_page_tab_holder.*
import kotlin.math.pow


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
